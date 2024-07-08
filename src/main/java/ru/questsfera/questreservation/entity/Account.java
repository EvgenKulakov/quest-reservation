package ru.questsfera.questreservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.validator.Patterns;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts", schema = "quest_reservations_db")
@JsonIgnoreProperties({"emailAndLogin", "password", "firstName", "lastName", "phone", "role", "companyId", "quests"})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = Patterns.EMAIL, message = "*Проверьте правильное написание Email")
    @Column(name = "email_login")
    private String emailLogin;

    @Pattern(regexp = Patterns.PASSWORD, message = "*Пароль минимум 8 символов без пробелов")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "*Введите имя")
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "company_id")
    private Integer companyId;

    @ManyToMany
    @JoinTable(name = "account_quest",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "quest_id"))
    private List<Quest> quests = new ArrayList<>();

    @Getter
    @AllArgsConstructor
    public enum Role {
        ROLE_OWNER("Владелец"),
        ROLE_ADMIN("Админ"),
        ROLE_USER("Пользователь");

        private final String text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return id != null && Objects.equals(getId(), account.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
