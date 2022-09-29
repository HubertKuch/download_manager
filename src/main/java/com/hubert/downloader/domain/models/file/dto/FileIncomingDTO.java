package com.hubert.downloader.domain.models.file.dto;

import com.hubert.downloader.domain.models.tokens.Token;

public record FileIncomingDTO (
        String file,
        String folderId,
        String account,
        Token token
) {}
