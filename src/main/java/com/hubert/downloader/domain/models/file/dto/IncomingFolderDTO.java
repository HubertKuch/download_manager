package com.hubert.downloader.domain.models.file.dto;

import com.hubert.downloader.domain.models.file.vo.PasswordData;

public record IncomingFolderDTO(
        String url,
        String account,
        String name,
        String folderId,
        PasswordData passwordData
) {}
