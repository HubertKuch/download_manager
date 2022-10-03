package com.hubert.downloader.domain.models.file.dto;

public record IncomingFolderDTO(
        String url,
        String account,
        String folderId
) {}
