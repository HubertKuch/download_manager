package com.hubert.downloader.domain.models.user.dto;

import com.hubert.downloader.domain.Transfer;
import com.hubert.downloader.domain.models.user.UserRole;

public record NewUserDTO(
        Transfer transfer,
        UserRole role
) {}
