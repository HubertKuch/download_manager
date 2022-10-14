package com.hubert.downloader.domain.models.file.dto;

public record FileToDeleteDTO(
        String folderId,
        String fileId
) {}