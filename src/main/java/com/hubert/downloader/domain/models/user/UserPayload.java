package com.hubert.downloader.domain.models.user;

import com.hubert.downloader.domain.Transfer;

import java.util.Date;

public record UserPayload(
        String id,
        String accessCode,
        Transfer transfer,
        UserRole role,
        Date expiringDate,
        Boolean hasActiveAccount
) {}
