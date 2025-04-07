package ru.questsfera.questreservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.validator.Patterns;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {

    private Integer id;

    @Pattern(regexp = Patterns.EMAIL, message = "*Проверьте правильное написание Email")
    private String login;

    @Pattern(regexp = Patterns.PASSWORD, message = "*Пароль минимум 8 символов без пробелов")
    private String password;

    @NotBlank(message = "*Введите имя")
    private String firstName;

    private String lastName;

    private String phone;

    private Account.Role role;

    private Company company;

    private Set<Quest> quests = new TreeSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountDTO account)) return false;
        return id != null && Objects.equals(getId(), account.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
