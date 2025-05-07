package ru.questsfera.questreservation.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Status;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestFormDTO {

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
