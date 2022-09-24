package com.hubert.downloader.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record InformationSize(InformationUnit unit, Float size) {

    public InformationSize parseTo(InformationUnit toUnit) {
        List<InformationUnit> units = Arrays.asList(InformationUnit.values());

        Float sizeOf = units.indexOf(unit) > units.indexOf(toUnit)
                ? parseSizeInDescending(units, toUnit)
                : parseInNormalOrder(units, toUnit);

        return new InformationSize(toUnit, sizeOf);
    }

    private Float parseSizeInDescending(List<InformationUnit> units, InformationUnit toUnit) {
        Float sizeOf = size;

        Collections.reverse(units);

        for (InformationUnit unit : units) {
            if (unit.equals(toUnit)) break;

            sizeOf = sizeOf * 1024;
        }

        return sizeOf;
    }

    private Float parseInNormalOrder(List<InformationUnit> units, InformationUnit toUnit) {
        Float sizeOf = size;

        for (InformationUnit unit : units.subList(units.indexOf(unit), units.size()-1)) {
            if (unit.equals(toUnit)) break;

            sizeOf = sizeOf / 1024;
        }

        return sizeOf;
    }
}
