package com.hubert.downloader.domain.models.file;

import com.hubert.downloader.domain.models.file.dto.FileWithoutPath;
import com.hubert.downloader.domain.models.file.dto.FolderWithFilesWithoutPaths;

import java.util.List;

public record Folder(
        String id,
        String url,
        String account,
        String name,
        List<File> files
) {
    public FolderWithFilesWithoutPaths parseExcludingPaths() {
        return new FolderWithFilesWithoutPaths(
                id,
                url,
                account,
                name,
                files.stream()
                        .map(file -> new FileWithoutPath(
                                file.getId(),
                                file.getName(),
                                file.getExtension(),
                                file.getSize()
                        )).toList()
        );
    }
}
