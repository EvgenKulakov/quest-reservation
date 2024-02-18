package ru.questsfera.questreservation.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.entity.Status;
import ru.questsfera.questreservation.entity.User;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class QuestForm {

    @NotBlank(message = "*Обязательное поле")
    private String questName;

    @Min(value = 1, message = "*Минимум 1")
    @Max(value = 100, message = "*Максимум 100")
    @NotNull(message = "*Обязательное поле")
    private Integer minPersons;

    @Min(value = 1, message = "*Минимум 1")
    @Max(value = 100, message = "*Максимум 100")
    @NotNull(message = "*Обязательное поле")
    private Integer maxPersons;

    private Boolean errorCountPersons;

    private Boolean onlySecondPageError;

    private Set<Status> statuses;

    @NotNull(message = "*Обязательное поле")
    private LocalTime autoBlock;

    private Set<User> users = new HashSet<>();

    private SlotList slotList = new SlotList();

    private SlotListTypeBuilder typeBuilder;

    public void setStartValues() {
        this.statuses = Status.getDefaultStatuses();
        this.autoBlock = LocalTime.MIN;
        this.typeBuilder = SlotListTypeBuilder.EQUAL_DAYS;
    }
}
