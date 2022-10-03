package com.hubert.downloader.domain.models.user.dto;

import com.hubert.downloader.domain.Transfer;
import com.hubert.downloader.domain.models.file.dto.FileWithoutPath;
import com.hubert.downloader.domain.models.user.UserRole;

import java.util.Date;
import java.util.List;

public record UserWithoutPathInFilesDTO(
        String id,
        String accessCode,
        Transfer transfer,
        List<FileWithoutPath> files,
        UserRole role,
        Date expiringDate,
        Boolean hasActiveAccount
) {}
