package com.hubert.downloader.domain.models.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hubert.downloader.domain.Transfer;
import com.hubert.downloader.domain.models.user.UserRole;

import java.util.Date;

public record NewUserDTO(
        Transfer transfer,
        UserRole role,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        Date expiringDate
) {}
