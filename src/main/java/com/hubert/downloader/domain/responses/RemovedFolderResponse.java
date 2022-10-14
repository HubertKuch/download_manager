package com.hubert.downloader.domain.responses;

import com.hubert.downloader.domain.models.file.Folder;

public record RemovedFolderResponse(
    String message,
    Folder folder
) {}