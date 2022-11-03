package com.hubert.downloader.domain.models.file.dto;

import com.hubert.downloader.domain.models.file.RequiringPassword;
import com.hubert.downloader.domain.models.file.vo.PasswordData;

public record IncomingFolderDTO(
        String url,
        String account,
        String name,
        String folderId,
        PasswordData passwordData
) implements RequiringPassword {

    @Override
    public PasswordData getPasswordData() {
        return passwordData;
    }
}
