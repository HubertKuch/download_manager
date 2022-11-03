package com.hubert.downloader.domain.models.file;

import com.hubert.downloader.domain.InformationSize;
import com.hubert.downloader.domain.models.file.vo.PasswordData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private UUID id;
    private Long hamsterId;
    private String name;
    private String extension;
    private String path;
    private InformationSize size;
    private PasswordData passwordData;

    public File(Long hamsterId, String name, String path, InformationSize size, PasswordData passwordData) {
        this.hamsterId = hamsterId;
        this.name = name;
        this.extension = getExtensionFromFileName(name);
        this.size = size;
        this.path = path;
        this.passwordData = passwordData;
    }

    public File(Long hamsterId, UUID id, String name, String path, InformationSize size) {
        this.hamsterId = hamsterId;
        this.id = id;
        this.name = name;
        this.extension = getExtensionFromFileName(name);
        this.path = path;
        this.size = size;
    }

    private String getExtensionFromFileName(String filename) {
        String[] filenameFragmentsSplitByDot = filename.split("\\.");

        return filenameFragmentsSplitByDot[filenameFragmentsSplitByDot.length-1];
    }
}
