package com.hubert.downloader.models;

import com.hubert.downloader.domain.InformationSize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private String id;
    private String name;
    private String extension;
    private InformationSize size;
}
