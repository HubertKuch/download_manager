package com.hubert.downloader.domain.models.file.dto;

import com.hubert.downloader.domain.models.file.RequiringPassword;
import com.hubert.downloader.domain.models.file.vo.PasswordData;

public record FileIncomingDTO (
        String file,
        String folderId,
        String account,
        PasswordData passwordData
) implements RequiringPassword {
    @Override
    public PasswordData getPasswordData() {
        return passwordData;
    }
}
