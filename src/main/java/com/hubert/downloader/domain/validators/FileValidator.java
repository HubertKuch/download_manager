package com.hubert.downloader.domain.validators;

import com.hubert.downloader.models.File;
import com.hubert.downloader.models.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class FileValidator {

    public boolean userCanDownloadAFile(final User user, final File file) {

        return user.isUserCanDownloadAFile(file);
    }
}
