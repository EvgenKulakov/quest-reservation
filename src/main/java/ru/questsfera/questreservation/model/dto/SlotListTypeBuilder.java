package ru.questsfera.questreservation.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SlotListTypeBuilder {

    EQUAL_DAYS("Одинаковые дни"),
    WEEKDAYS_WEEKENDS("Будние-выходные"),
    DIFFERENT_DAYS("Разные дни");

    private final String text;
}
