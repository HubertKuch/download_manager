package com.hubert.downloader.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InformationSizeTest {

    @Test
    void parseBytesToKilobytes() {
        InformationSize informationSize = new InformationSize(InformationUnit.BYTE, 1024F);

        assertEquals(
                1,
                informationSize.parseTo(InformationUnit.KILO_BYTE).size()
        );
    }

    @Test
    void parseKilobytesToMegabytes() {
        InformationSize informationSize = new InformationSize(InformationUnit.KILO_BYTE, 1024F);

        assertEquals(1, informationSize.parseTo(InformationUnit.MEGA_BYTE).size());
    }

    @Test
    void parseMegabytesToBytes() {
        InformationSize informationSize = new InformationSize(InformationUnit.MEGA_BYTE, 1F);

        assertEquals(1024 * 1024, informationSize.parseTo(InformationUnit.KILO_BYTE).size());
    }
}