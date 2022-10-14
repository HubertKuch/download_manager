package com.hubert.downloader.domain.models.history;

import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.Folder;

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

    public static History ofDownloadedFiles(File file) {
        return ofDownloadedFiles(List.of(file));
    }

    public static History ofAddedFiles(List<File> downloadedFiles) {
        return new History(
                UUID.randomUUID(),
                downloadedFiles,
                HistoryType.ADDED,
                new Date(System.currentTimeMillis())
        );
    }

    public static History ofAddedFiles(File file) {
        return History.ofAddedFiles(List.of(file));
    }

    public static History ofDeletedFolder(List<File> deletedFolder) {
        return new History(
                UUID.randomUUID(),
                deletedFolder,
                HistoryType.DELETE_FOLDER,
                new Date(System.currentTimeMillis())
        );
    }

    public static History ofDeletedFolder(Folder folder) {
        return ofDeletedFolder(folder.files());
    }

    public static History ofDeletedFile(File file) {
        return new History(
                UUID.randomUUID(),
                List.of(file),
                HistoryType.DELETE_FILE,
                new Date(System.currentTimeMillis())
        );
    }
}