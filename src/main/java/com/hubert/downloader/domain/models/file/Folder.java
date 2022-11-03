package com.hubert.downloader.domain.models.file;

import com.hubert.downloader.domain.InformationSize;
import com.hubert.downloader.domain.InformationUnit;
import com.hubert.downloader.domain.models.file.dto.FileWithoutPath;
import com.hubert.downloader.domain.models.file.dto.FolderWithFilesWithoutPaths;
import com.hubert.downloader.domain.models.file.dto.IncomingFolderDTO;
import com.hubert.downloader.domain.models.file.vo.PasswordData;
import com.hubert.downloader.external.coreapplication.modelsgson.FolderDownloadChFile;
import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.AndroidApi;

import java.util.Date;
import java.util.List;

public record Folder(
        String id,
        String url,
        String account,
        String name,
        List<File> files,
        Date addedAt,
        PasswordData passwordData
) {

    public static Folder from(IncomingFolderDTO incomingFolderDTO, List<FolderDownloadChFile> requestedFiles) {
        return new Folder(
                incomingFolderDTO.folderId(),
                incomingFolderDTO.url(),
                incomingFolderDTO.account(),
                incomingFolderDTO.name(),
                requestedFiles.stream().map(file -> {
                    try {
                        return new File(
                                file.fileId,
                                file.fileName,
                                AndroidApi.getDownloadUrl(file.getId()).fileUrl,
                                new InformationSize(InformationUnit.KILO_BYTE, file.size),
                                incomingFolderDTO.passwordData()
                        );
                    } catch (Exception | PasswordRequiredException e) {
                        throw new RuntimeException(e);
                    }
                }).toList(),
                new Date(System.currentTimeMillis()),
                incomingFolderDTO.passwordData()
        );
    }

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
                                file.getSize(),
                                file.getPasswordData()
                        )).toList(),
                addedAt == null ?  new Date() : addedAt,
                passwordData
        );
    }
}
