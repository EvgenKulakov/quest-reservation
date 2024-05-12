package ru.questsfera.questreservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.validator.Patterns;
import ru.questsfera.questreservation.validator.ValidType;

@Getter
@Setter
@NoArgsConstructor
public class ReservationForm {

    private static final String ERROR_BLOCK_MESSAGE = "*Для блокировки все поля должны быть пустыми";

    private StatusType statusType;

    @NotBlank(message = "*Обязательное поле", groups = ValidType.SaveReserve.class)
    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = ValidType.BlockSlot.class)
    private String firstName;

    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = ValidType.BlockSlot.class)
    private String lastName;

    @Pattern(regexp = Patterns.PHONE,
            message = "*Введите номер телефона в формате +7хххххххххх",
            groups = ValidType.SaveReserve.class)
    @Size(max = 2, message = ERROR_BLOCK_MESSAGE, groups = ValidType.BlockSlot.class)
    private String phone = "+7";

    @Pattern(regexp = Patterns.EMAIL_OR_EMPTY,
            message = "*Проверьте правильное написание Email", groups = ValidType.SaveReserve.class)
    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = ValidType.BlockSlot.class)
    private String email;

    private Integer countPersons;

    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = ValidType.BlockSlot.class)
    private String adminComment;

    private String clientComment;
}
