package ru.questsfera.questreservation.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.validator.Patterns;

@Getter
@Setter
@NoArgsConstructor
public class AccountCreateForm {

    @Pattern(regexp = Patterns.EMAIL, message = "*Проверьте правильное написание Email")
    private String login;

    @Pattern(regexp = Patterns.PASSWORD, message = "*Пароль минимум 8 символов без пробелов")
    private String password;

    @NotBlank(message = "*Введите имя")
    private String firstName;

    private String lastName;

    private Account.Role role;

    @NotBlank(message = "*Введите название компании")
    private String companyName;
}
