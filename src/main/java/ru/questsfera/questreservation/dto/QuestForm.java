package ru.questsfera.questreservation.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Status;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private List<Status> statuses;

    @NotNull(message = "*Обязательное поле")
    private LocalTime autoBlock;

    private List<Account> accounts = new ArrayList<>();

    private SlotList slotList = new SlotList();

    private SlotListTypeBuilder typeBuilder;
}
