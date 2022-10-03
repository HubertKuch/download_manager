package com.hubert.downloader.domain.models.file;

import java.util.List;

public record Folder(
        String id,
        String url,
        String account,
        List<File> files
) {}
