package ru.questsfera.questreservation.dto;

public enum StatusType {
    EMPTY(0, "Свободно"),
    NEW_RESERVE(1, "Новый"),
    CANCEL(2, "Отменён"),
    CONFIRMED(3, "Подтверждён"),
    NOT_COME(4, "Не пришёл"),
    COMPLETED(5, "Завершён"),
    BLOCK(6, "Заблокирован"),
    MODIFIED(7, "Изменён слот");

    private Integer id;
    private String name;

    StatusType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
