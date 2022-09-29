package com.hubert.downloader.services;

import com.hubert.downloader.domain.InformationSize;
import com.hubert.downloader.domain.InformationUnit;
import com.hubert.downloader.domain.exceptions.FileNotFoundException;
import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.domain.models.file.dto.FileIncomingDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FileService {
    private final UserService userService;
    private final FileValidator fileValidator;
    @Value("${android.hamster.credentials.username}")
    private String username;
    @Value("${android.hamster.credentials.password}")
    private String password;

    public File getRequestedFile(FileIncomingDTO fileIncomingDTO) {
        try {
            AndroidApi.login(username, password);
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

            return new File(fileIncomingDTO.file(), downloadUrl.fileUrl, new InformationSize(
                    InformationUnit.KILO_BYTE,
                    requestedFile.get(0).size
            ));
        } catch (Exception | PasswordRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    public User downloadFile(final User user, final File file) throws UserCantDownloadFile {
        boolean userCanDownloadFile = fileValidator.userCanDownloadAFile(user, file);

        if(!userCanDownloadFile) {
            throw new UserCantDownloadFile("User doesn't have enough transfer to download a file.");
        }

        user.getTransfer().subtract(file.getSize());
        user.addFile(file);

        return userService.saveUser(user);
    }
}
