package ru.questsfera.questreservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ru.questsfera.questreservation.dto.AccountDTO;
import ru.questsfera.questreservation.validator.Patterns;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
@Entity
@Table(name = "accounts", schema = "quest_reservations_db")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = Patterns.EMAIL, message = "*Проверьте правильное написание Email")
    @Column(name = "login")
    private String login;

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
    private Set<Quest> quests = new TreeSet<>();

    public Account(AccountDTO accountDTO) {
        this.id = accountDTO.getId();
        this.login = accountDTO.getLogin();
        this.firstName = accountDTO.getFirstName();
        this.password = accountDTO.getPassword();
        this.lastName = accountDTO.getLastName();
        this.phone = accountDTO.getPhone();
        this.role = accountDTO.getRole();
        this.companyId = accountDTO.getCompany().getId();
        this.quests = accountDTO.getQuests();
    }

    @Getter
    @RequiredArgsConstructor
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
