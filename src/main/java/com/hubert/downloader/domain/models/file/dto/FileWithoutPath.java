package com.hubert.downloader.domain.models.file.dto;

import com.hubert.downloader.domain.InformationSize;
import com.hubert.downloader.domain.models.file.vo.PasswordData;

import java.util.UUID;

public record FileWithoutPath(
        UUID id,
        String name,
        String extension,
        InformationSize size,
        PasswordData passwordData
) {}
