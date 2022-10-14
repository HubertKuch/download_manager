package com.hubert.downloader.domain.models.file.dto;

import java.util.Date;
import java.util.List;

public record FolderWithFilesWithoutPaths(
        String id,
        String url,
        String account,
        String name,
        List<FileWithoutPath> files,
        Date addedAt
) {}
