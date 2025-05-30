package ru.questsfera.questreservation.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Company;
import ru.questsfera.questreservation.validator.Patterns;

import java.util.Objects;

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

    private Company company;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountCreateForm that = (AccountCreateForm) o;
        return Objects.equals(login, that.login)
                && Objects.equals(password, that.password)
                && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && role == that.role
                && Objects.equals(company, that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, firstName, lastName, role, company);
    }
}
