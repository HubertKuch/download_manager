package com.hubert.downloader.domain.responses;

import com.hubert.downloader.domain.models.file.File;

public record RemovedFileReponse(
        String message,
        File file
) {}