package com.hubert.downloader.domain.models.file.dto;


import com.hubert.downloader.domain.models.file.IncomingDataRequestWithPassword;
import com.hubert.downloader.domain.models.file.vo.PasswordData;

public record FileWithUrlAndPasswordInfo (
        String url,
        PasswordData passwordData
) implements IncomingDataRequestWithPassword {

    @Override
    public PasswordData getPasswordData() {
        return passwordData;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
