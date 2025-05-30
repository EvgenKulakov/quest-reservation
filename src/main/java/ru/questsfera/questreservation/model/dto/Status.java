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
public enum Status {
    EMPTY("Свободно"),
    NEW_RESERVE("Новый"),
    CANCEL("Отменён"),
    CONFIRMED("Подтверждён"),
    NOT_COME("Не пришёл"),
    COMPLETED("Завершён"),
    BLOCK("Заблокирован"),
    MODIFIED("Изменён слот");

    private final String text;

    public static final List<Status> DEFAULT_STATUSES = List.of(
            NEW_RESERVE, CANCEL, CONFIRMED, NOT_COME, COMPLETED
    );

    public static final List<Status> MANDATORY_STATUSES = List.of(
            NEW_RESERVE, CANCEL
    );

    @JsonCreator
    public static Status fromJson(@JsonProperty("name") String name) {
        return Status.valueOf(name);
    }

    public String getName() {
        return name();
    }
}
