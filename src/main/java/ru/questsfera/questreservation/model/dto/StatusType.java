package ru.questsfera.questreservation.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusType {
    EMPTY("Свободно"),
    NEW_RESERVE("Новый"),
    CANCEL("Отменён"),
    CONFIRMED("Подтверждён"),
    NOT_COME("Не пришёл"),
    COMPLETED("Завершён"),
    BLOCK("Заблокирован"),
    MODIFIED("Изменён слот");

    private final String text;

    public static final List<StatusType> DEFAULT_STATUSES = List.of(
            NEW_RESERVE, CANCEL, CONFIRMED, NOT_COME, COMPLETED
    );

    public static final List<StatusType> MANDATORY_STATUSES = List.of(
            NEW_RESERVE, CANCEL
    );

    @JsonCreator
    public static StatusType fromJson(@JsonProperty("name") String name) {
        return StatusType.valueOf(name);
    }

    public String getName() {
        return name();
    }
}
