package com.hubert.downloader.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record InformationSize(InformationUnit unit, Long size) {

    public InformationSize parseTo(InformationUnit toUnit) {
        List<InformationUnit> units = Arrays.asList(InformationUnit.values());

        Long sizeOf = units.indexOf(unit) > units.indexOf(toUnit)
                ? parseSizeInDescending(units, toUnit)
                : parseInNormalOrder(units, toUnit);

        return new InformationSize(toUnit, sizeOf);
    }

    private Long parseSizeInDescending(List<InformationUnit> units, InformationUnit toUnit) {
        Long sizeOf = size;

        Collections.reverse(units);

        for (InformationUnit unit : units.subList(units.indexOf(unit), units.size()-1)) {
            if (unit.equals(toUnit)) break;

            sizeOf = sizeOf * 1024;
        }

        return sizeOf;
    }

    private Long parseInNormalOrder(List<InformationUnit> units, InformationUnit toUnit) {
        Long sizeOf = size;

        for (InformationUnit unit : units.subList(units.indexOf(unit), units.size()-1)) {
            if (unit.equals(toUnit)) break;

            sizeOf = sizeOf / 1024;
        }

        return sizeOf;
    }
}
