package ru.questsfera.questreservation.dto;

public enum StatusType {
    EMPTY,
    BLOCK,
    MODIFIED,
    NEW_RESERVE("Новый"),
    CANCEL("Отменён"),
    CONFIRMED("Подтверждён"),
    COMPLETED("Завершён");

    private String name;

    StatusType(String name) {
        this.name = name;
    }

    StatusType() {}

    public String getName() {
        return name;
    }
}
