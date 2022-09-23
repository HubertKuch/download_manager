package com.hubert.downloader.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InformationSize {
    private InformationUnit unit;
    private Float size;
}
