package com.hubert.downloader.domain.models.user.responses;

import com.hubert.downloader.domain.Transfer;
import com.hubert.downloader.domain.models.user.UserRole;

import java.util.Date;
import java.util.List;

public record UserDataEntity(
        String id,
        String accessCode,
        Transfer transfer,
        List<Transfer> otherTransferSizes,
        UserRole userRole,
        Date expiringDate,
        Boolean hasActiveAccount
) {}
