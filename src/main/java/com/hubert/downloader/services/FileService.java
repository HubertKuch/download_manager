package com.hubert.downloader.services;

import com.hubert.downloader.domain.InformationSize;
import com.hubert.downloader.domain.InformationUnit;
import com.hubert.downloader.domain.exceptions.FileNotFoundException;
import com.hubert.downloader.domain.exceptions.FolderRequiresPasswordException;
import com.hubert.downloader.domain.exceptions.InvalidRequestDataException;
import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.Folder;
import com.hubert.downloader.domain.models.file.RequiringPassword;
import com.hubert.downloader.domain.models.file.dto.FileIncomingDTO;
import com.hubert.downloader.domain.models.file.dto.IncomingFolderDTO;
import com.hubert.downloader.domain.models.file.vo.PasswordData;
import com.hubert.downloader.domain.models.history.History;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.domain.validators.FileValidator;
import com.hubert.downloader.external.coreapplication.models.AccountsListItem;
import com.hubert.downloader.external.coreapplication.modelsgson.FolderDownload;
import com.hubert.downloader.external.coreapplication.modelsgson.FolderDownloadChFile;
import com.hubert.downloader.external.coreapplication.modelsgson.GetDownloadUrl;
import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.*;
import com.hubert.downloader.utils.HamsterPasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class FileService {
    private final UserService userService;
    private final FileValidator fileValidator;

    public File getRequestedFile(FileIncomingDTO fileIncomingDTO) throws FolderRequiresPasswordException {
        try {
            AccountsListItem account = AndroidApi.searchForAccount(fileIncomingDTO.account());
            FolderDownload folder = AndroidApi.getFolderDownload(account.getAccountId(), fileIncomingDTO.folderId(), "");

            List<FolderDownloadChFile> requestedFileMatches = folder
                    .files
                    .stream()
                    .filter(file -> Objects.equals(file.getName(), fileIncomingDTO.file()))
                    .toList();

            if (requestedFileMatches.isEmpty()) {
                throw new FileNotFoundException("File not found");
            }

            FolderDownloadChFile requestedFile = requestedFileMatches.get(0);

            HamsterPasswordUtils.operateOnFile(fileIncomingDTO.getPasswordData(), fileIncomingDTO.folderId(), account.getAccountId());
            GetDownloadUrl downloadUrl = AndroidApi.getDownloadUrl(requestedFile.getId());

            return new File(
                    requestedFileMatches.get(0).fileId,
                    fileIncomingDTO.file(),
                    downloadUrl.fileUrl,
                    new InformationSize(
                            InformationUnit.KILO_BYTE,
                            requestedFileMatches.get(0).size
                    ),
                    fileIncomingDTO.passwordData()
            );
        } catch (Exception | PasswordRequiredException e) {
            throw new FolderRequiresPasswordException("");
        }
    }

    public Folder getRequestedFolder(IncomingFolderDTO incomingFolderDTO) throws FolderRequiresPasswordException {
        try {
            AccountsListItem account = AndroidApi.searchForAccount(incomingFolderDTO.account());
            FolderDownload folder = AndroidApi.getFolderDownload(account.getAccountId(), incomingFolderDTO.folderId(), incomingFolderDTO.name());

            List<FolderDownloadChFile> requestedFiles = folder.files;

            return Folder.from(incomingFolderDTO, requestedFiles);
        } catch (PasswordRequiredException | Exception e) {
            throw new FolderRequiresPasswordException("Folder requires a password data.");
        }
    }

    public User addFile(final User user, final File file, final Folder folder) throws UserCantDownloadFile {
        user.addFile(folder, file);
        file.setId(UUID.randomUUID());

        return userService.saveUser(user);
    }

    public File downloadFile(final User user, final String fileId) throws UserCantDownloadFile, FileNotFoundException {
        List<Folder> matchedFolders = user
                .getFolders()
                .stream()
                .filter(folder -> folder.files().stream().anyMatch(file -> file.getId().toString().equals(fileId)))
                .toList();

        if (matchedFolders.isEmpty()) {
            throw new FileNotFoundException(String.format("File with id - %s - not found.", fileId));
        }

        Folder folder = matchedFolders.get(0);

        List<File> matchedFiles = folder
                .files()
                .stream()
                .filter(file -> file.getId().toString().equals(fileId))
                .toList();

        if (matchedFiles.isEmpty()) {
            throw new FileNotFoundException(String.format("File with id - %s - not found.", fileId));
        }

        File file = matchedFiles.get(0);

        boolean userCanDownloadFile = fileValidator.userCanDownloadAFile(user, file);

        try {
            AccountsListItem accountsListItem = AndroidApi.searchForAccount(folder.account());

            if (file.getPasswordData() != null && file.getPasswordData().getHasPassword()) {
                HamsterPasswordUtils.operateOnFile(file, folder, accountsListItem);
            }

            GetDownloadUrl url = AndroidApi.getDownloadUrl(file.getHamsterId());

            if (!userCanDownloadFile) {
                throw new UserCantDownloadFile("User doesn't have enough transfer to download a file.");
            }

            if (user.getTransfer().getTransfer().size() >= 0) {
                user.getTransfer().subtract(file.getSize());
            }

            user.addHistory(History.ofDownloadedFiles(file));

            file.setPath(url.fileUrl);

            userService.saveUser(user);

            return file;
        } catch (Exception | PasswordRequiredException e) {
            System.out.println(e);
            throw new UserCantDownloadFile("User cant download a file. File missing.");
        }
    }

    public Folder downloadFolder(final User user, final Folder folder) throws UserCantDownloadFile {
        boolean isCanDownloadAFolder = fileValidator.userCanDownloadAFolder(user, folder);

        if (!isCanDownloadAFolder) {
            throw new UserCantDownloadFile("User doesn't have enough transfer to download a file.");
        }

        folder.files().forEach(file -> {
            user.getTransfer().subtract(file.getSize());
        });

        userService.saveUser(user);

        return folder;
    }

    public Optional<Folder> findFolderById(User user, String id) {
        List<Folder> matchedFolders = user.getFolders().stream().filter(folder -> folder.id().equals(id)).toList();

        if (matchedFolders.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(matchedFolders.get(0));
    }

    public Optional<File> findFileById(User user, String id) {
        List<File> matchedFiles = user
                .getFolders()
                .stream()
                .flatMap(folder -> folder.files().stream())
                .filter(file -> file.getId().toString().equals(id))
                .toList();

        if (matchedFiles.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(matchedFiles.get(0));
    }

    public void removeFolder(User user, Folder folder) {
        user.removeFolder(folder);
    }

    public void removeFile(User user, Folder folder, File file) throws InvalidRequestDataException {
        user.removeFile(folder, file);
    }
}
