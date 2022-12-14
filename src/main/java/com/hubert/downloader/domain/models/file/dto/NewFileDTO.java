package com.hubert.downloader.domain.models.file.dto;

import com.hubert.downloader.domain.models.file.IncomingDataRequestWithPassword;
import com.hubert.downloader.domain.models.file.vo.PasswordData;

public record NewFileDTO(
        String url,
        String fileName,
        PasswordData passwordData
) implements IncomingDataRequestWithPassword {

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public PasswordData getPasswordData() {
        return passwordData;
    }
}
