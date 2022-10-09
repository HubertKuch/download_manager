package com.hubert.downloader.domain.models.file;

import com.hubert.downloader.domain.models.file.vo.PasswordData;

public interface RequiringPassword {

    PasswordData getPasswordData();
}
