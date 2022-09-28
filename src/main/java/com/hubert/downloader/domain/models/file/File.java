package com.hubert.downloader.domain.models.file;

import com.hubert.downloader.domain.InformationSize;
import lombok.Data;

@Data
public class File {
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

    private String getExtensionFromFileName(String filename) {
        String[] filenameFragmentsSplitByDot = filename.split("\\.");

        return filenameFragmentsSplitByDot[filenameFragmentsSplitByDot.length-1];
    }
}
