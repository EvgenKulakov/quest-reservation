package ru.questsfera.questreservation.dto;

public enum StatusType {
    EMPTY,
    BLOCK,
    MODIFIED,
    NEW_RESERVE(1, "Новый"),
    CANCEL(2, "Отменён"),
    CONFIRMED(3, "Подтверждён"),
    NOT_COME(4, "Не пришёл"),
    COMPLETED(5, "Завершён");

    private Integer id;
    private String name;

    StatusType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    StatusType() {}

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
