package ru.questsfera.questreservation.dto;

public enum SlotListTypeBuilder {

    EQUAL_DAYS("Одинаковые дни"),
    WEEKDAYS_WEEKENDS("Будние-выходные"),
    DIFFERENT_DAYS("Разные дни");

    private final String text;

    SlotListTypeBuilder(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
