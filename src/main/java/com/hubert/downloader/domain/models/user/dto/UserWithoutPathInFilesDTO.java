package com.hubert.downloader.domain.models.user.dto;

import com.hubert.downloader.domain.Transfer;
import com.hubert.downloader.domain.models.file.dto.FolderWithFilesWithoutPaths;
import com.hubert.downloader.domain.models.user.UserRole;

import java.util.Date;
import java.util.List;

public record UserWithoutPathInFilesDTO(
        String id,
        String accessCode,
        Transfer transfer,
        List<Transfer> otherTransferSizes,
        List<FolderWithFilesWithoutPaths> folders,
        UserRole role,
        Date expiringDate,
        Boolean hasActiveAccount
) {}
