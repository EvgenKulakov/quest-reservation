package ru.questsfera.questreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlotListTypeBuilder {

    EQUAL_DAYS("Одинаковые дни"),
    WEEKDAYS_WEEKENDS("Будние-выходные"),
    DIFFERENT_DAYS("Разные дни");

    private final String text;
}
