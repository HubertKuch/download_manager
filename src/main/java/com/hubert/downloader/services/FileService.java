package com.hubert.downloader.services;

import com.hubert.downloader.domain.InformationSize;
import com.hubert.downloader.domain.InformationUnit;
import com.hubert.downloader.domain.exceptions.FileNotFoundException;
import com.hubert.downloader.domain.exceptions.InvalidRequestDataException;
import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.domain.models.file.Folder;
import com.hubert.downloader.domain.models.file.dto.FileIncomingDTO;
import com.hubert.downloader.domain.models.file.dto.IncomingFolderDTO;
import com.hubert.downloader.domain.validators.FileValidator;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.external.coreapplication.models.AccountsListItem;
import com.hubert.downloader.external.coreapplication.modelsgson.FolderDownload;
import com.hubert.downloader.external.coreapplication.modelsgson.FolderDownloadChFile;
import com.hubert.downloader.external.coreapplication.modelsgson.GetDownloadUrl;
import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.AndroidApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileService {
    private final UserService userService;
    private final FileValidator fileValidator;

    public File getRequestedFile(FileIncomingDTO fileIncomingDTO) {
        try {
            AccountsListItem account = AndroidApi.searchForAccount(fileIncomingDTO.account());
            FolderDownload folder = AndroidApi.getFolderDownload(account.getAccountId(), fileIncomingDTO.folderId(), "");

            List<FolderDownloadChFile> requestedFile = folder
                    .files
                    .stream()
                    .filter(file -> Objects.equals(file.getName(), fileIncomingDTO.file()))
                    .toList();

            if (requestedFile.isEmpty()) {
                throw new FileNotFoundException("File not found");
            }

            GetDownloadUrl downloadUrl = AndroidApi.getDownloadUrl(requestedFile.get(0).getId());

            return new File(
                    requestedFile.get(0).fileId,
                    fileIncomingDTO.file(),
                    downloadUrl.fileUrl,
                    new InformationSize(
                            InformationUnit.KILO_BYTE,
                            requestedFile.get(0).size
                    ));
        } catch (Exception | PasswordRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    public Folder getRequestedFolder(IncomingFolderDTO incomingFolderDTO) {
        try {
            AccountsListItem account = AndroidApi.searchForAccount(incomingFolderDTO.account());
            FolderDownload folder = AndroidApi.getFolderDownload(account.getAccountId(), incomingFolderDTO.folderId(), incomingFolderDTO.name());

            List<FolderDownloadChFile> requestedFiles = folder.files;

            return Folder.from(incomingFolderDTO, requestedFiles);
        } catch (Exception | PasswordRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    public User addFile(final User user, final File file, final Folder folder) throws UserCantDownloadFile {
        boolean userCanDownloadFile = fileValidator.userCanDownloadAFile(user, file);

        if (!userCanDownloadFile) {
            throw new UserCantDownloadFile("User doesn't have enough transfer to download a file.");
        }

        user.addFile(folder, file);
        file.setId(UUID.randomUUID());

        return userService.saveUser(user);
    }

    public File downloadFile(final User user, final File file) throws UserCantDownloadFile {
        boolean userCanDownloadFile = fileValidator.userCanDownloadAFile(user, file);

        if (!userCanDownloadFile) {
            throw new UserCantDownloadFile("User doesn't have enough transfer to download a file.");
        }

        if (user.getTransfer().getTransfer().size() >= 0) {
            user.getTransfer().subtract(file.getSize());
        }

        userService.saveUser(user);

        return file;
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
