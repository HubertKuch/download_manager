package com.hubert.downloader.domain.models.file;

import com.hubert.downloader.domain.InformationSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private UUID id;
    private String name;
    private String extension;
    private String path;
    private InformationSize size;

    public File(String name, String path, InformationSize size) {
        this.name = name;
        this.extension = getExtensionFromFileName(name);
        this.size = size;
        this.path = path;
    }

    public File(UUID id, String name, String path, InformationSize size) {
        this.extension = getExtensionFromFileName(name);
        this.id = id;
        this.name = name;
        this.path = path;
        this.size = size;
    }

    private String getExtensionFromFileName(String filename) {
        String[] filenameFragmentsSplitByDot = filename.split("\\.");

        return filenameFragmentsSplitByDot[filenameFragmentsSplitByDot.length-1];
    }
}
