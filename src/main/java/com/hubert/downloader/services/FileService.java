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
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.domain.validators.FileValidator;
import com.hubert.downloader.external.coreapplication.models.AccountsListItem;
import com.hubert.downloader.external.coreapplication.modelsgson.FolderDownload;
import com.hubert.downloader.external.coreapplication.modelsgson.FolderDownloadChFile;
import com.hubert.downloader.external.coreapplication.modelsgson.GetDownloadUrl;
import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.*;
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

            if (fileIncomingDTO.passwordData().getHasPassword()) {
                WebApi.postFolderPassword(
                        account.getAccountName(),
                        fileIncomingDTO.folderId(),
                        requestedFile.getName(),
                        fileIncomingDTO.passwordData().getFolderPassword()
                );

                AndroidApi.postFolderPassword(
                        account.getAccountId(),
                        fileIncomingDTO.folderId(),
                        fileIncomingDTO.passwordData().getFolderPassword()
                );
            }

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
        } catch (Exception | PasswordRequiredException | TryAgainException | TooFastRequestsException |
                 ReloginRequiredException | CopyingForbiddenException e) {
            throw new FolderRequiresPasswordException("");
        }
    }

    public Folder getRequestedFolder(IncomingFolderDTO incomingFolderDTO) throws FolderRequiresPasswordException {
        try {
            AccountsListItem account = AndroidApi.searchForAccount(incomingFolderDTO.account());

            if (incomingFolderDTO.passwordData().getHasPassword()) {
                if (incomingFolderDTO.folderId().equals("0")) {
                    provideUserPassword(account, incomingFolderDTO);
                }

//                    provideFolderPassword(account, incomingFolderDTO);
//                }
            }

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

    private void provideUserPassword(AccountsListItem account, RequiringPassword requiringPassword) throws FolderRequiresPasswordException {
        try {
            AndroidApi.postPassword(account.getAccountId(), "0", requiringPassword.getPasswordData().getHamsterPassword());
        } catch (Exception | PasswordRequiredException e) {
            throw new FolderRequiresPasswordException("");
        }
    }

    private void provideFolderPassword(AccountsListItem account, IncomingFolderDTO folderDTO) throws FolderRequiresPasswordException {
        try {
            AndroidApi.postFolderPassword(account.getAccountId(), folderDTO.folderId(), folderDTO.getPasswordData().getFolderPassword());
            WebApi.postFolderPassword(account.getAccountName(), folderDTO.folderId(), folderDTO.name(), folderDTO.getPasswordData().getFolderPassword());
        } catch (Exception | PasswordRequiredException | CopyingForbiddenException | ReloginRequiredException | TryAgainException | TooFastRequestsException e) {
            throw new FolderRequiresPasswordException("");
        }
    }
}
