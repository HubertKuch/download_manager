package com.hubert.downloader.domain.models.file.dto;

public record FileIncomingDTO (
        String file,
        String folderId,
        String account
) {}
