package com.hubert.downloader.services;

import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.domain.validators.FileValidator;
import com.hubert.downloader.models.File;
import com.hubert.downloader.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileService {
    private final UserService userService;
    private final FileValidator fileValidator;

    public User downloadFile(final User user, final File file) throws UserCantDownloadFile {
        boolean userCanDownloadFile = fileValidator.userCanDownloadAFile(user, file);

        if(!userCanDownloadFile) {
            throw new UserCantDownloadFile("User doesn't have enough transfer to download a file.");
        }

        user.getFiles().add(file);
        user.getTransfer().subtract(file.getSize());

        return userService.saveUser(user);
    }
}
