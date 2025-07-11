package ru.questsfera.questreservation.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ru.questsfera.questreservation.validator.Patterns;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_seq")
    @SequenceGenerator(name = "accounts_seq", sequenceName = "accounts_id_seq", allocationSize = 1)
    private Integer id;

    // TODO dto for save update
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
