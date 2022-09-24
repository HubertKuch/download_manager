package com.hubert.downloader.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InformationUnit {
    BYTE(1L),
    KILO_BYTE(BYTE.base << 10),
    MEGA_BYTE(KILO_BYTE.base << 10),
    GIGA_BYTE(MEGA_BYTE.base << 10);

    private final Long base;
}
