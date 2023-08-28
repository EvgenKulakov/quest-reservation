package ru.questsfera.questreservation.dto;

public enum StatusType {
    EMPTY,
    BLOCK,
    MODIFIED,
    NEW_RESERVE("Новый"),
    CONFIRMED("Подтверждён"),
    CANCEL("Отменён"),
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
