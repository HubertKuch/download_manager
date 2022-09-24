package com.hubert.downloader.models;

import com.hubert.downloader.domain.InformationSize;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class File {
    private String id;
    private String name;
    private String extension;
    private InformationSize size;

    public File(String id, String name, InformationSize size) {
        this.id = id;
        this.name = name;
        this.extension = getExtensionFromFileName(name);
        this.size = size;
    }

    private String getExtensionFromFileName(String filename) {
        String[] filenameFragmentsSplitByDot = filename.split("\\.");

        return filenameFragmentsSplitByDot[filenameFragmentsSplitByDot.length-1];
    }
}
