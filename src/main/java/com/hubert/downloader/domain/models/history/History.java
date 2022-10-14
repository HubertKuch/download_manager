package com.hubert.downloader.domain.models.history;

import com.hubert.downloader.domain.models.file.File;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record History(
        UUID id,
        List<File> downloadedFiles,
        HistoryType historyType,
        Date at
) {
    public static History ofDownloadedFiles(List<File> downloadedFiles) {
        return new History(
                UUID.randomUUID(),
                downloadedFiles,
                HistoryType.DOWNLOAD,
                new Date(System.currentTimeMillis())
        );
    }

    public static History ofAddedFiles(List<File> downloadedFiles) {
        return new History(
                UUID.randomUUID(),
                downloadedFiles,
                HistoryType.ADDED,
                new Date(System.currentTimeMillis())
        );
    }
}