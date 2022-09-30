package com.hubert.downloader.domain.models.file.dto;

import com.hubert.downloader.domain.InformationSize;

import java.util.UUID;

public record FileWithoutPath(
        UUID id,
        String name,
        String extension,
        InformationSize size
) {}
