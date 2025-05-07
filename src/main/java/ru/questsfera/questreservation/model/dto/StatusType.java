package ru.questsfera.questreservation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusType {
    EMPTY(0, "Свободно"),
    NEW_RESERVE(1, "Новый"),
    CANCEL(2, "Отменён"),
    CONFIRMED(3, "Подтверждён"),
    NOT_COME(4, "Не пришёл"),
    COMPLETED(5, "Завершён"),
    BLOCK(6, "Заблокирован"),
    MODIFIED(7, "Изменён слот");

    private final Integer id;
    private final String text;
}
