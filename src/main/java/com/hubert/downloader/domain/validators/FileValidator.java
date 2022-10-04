package com.hubert.downloader.domain.validators;

import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.Folder;
import com.hubert.downloader.domain.models.user.User;
import org.springframework.stereotype.Service;

@Service
public class FileValidator {

    public boolean userCanDownloadAFile(final User user, final File file) {
        return user.isUserCanDownloadAFile(file);
    }

    public boolean userCanDownloadAFolder(final User user, final Folder folder) {
        return user.isUserCanDownloadAFolder(folder);
    }
}
