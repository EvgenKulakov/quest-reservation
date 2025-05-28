package ru.questsfera.questreservation.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
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

    public String getName() {
        return name();
    }

    @JsonCreator
    public static StatusType fromJson(@JsonProperty("name") String name) {
        return StatusType.valueOf(name);
    }

    public static List<StatusType> DEFAULT_STATUSES() {
        List<StatusType> defaultStatuses = new ArrayList<>();
        defaultStatuses.add(StatusType.NEW_RESERVE);
        defaultStatuses.add(StatusType.CANCEL);
        defaultStatuses.add(StatusType.CONFIRMED);
        defaultStatuses.add(StatusType.NOT_COME);
        defaultStatuses.add(StatusType.COMPLETED);
        return defaultStatuses;
    }

    public static List<StatusType> MANDATORY_STATUSES() {
        List<StatusType> mandatoryStatuses = new ArrayList<>();
        mandatoryStatuses.add(StatusType.NEW_RESERVE);
        mandatoryStatuses.add(StatusType.CANCEL);
        return mandatoryStatuses;
    }
}
