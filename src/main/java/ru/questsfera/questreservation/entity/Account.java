package ru.questsfera.questreservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.validator.Patterns;

import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "accounts", schema = "quest_reservations_db")
@JsonIgnoreProperties({"emailAndLogin", "password", "firstName", "lastName", "phone", "role", "company", "quests"})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = Patterns.EMAIL, message = "*Проверьте правильное написание Email")
    @Column(name = "email_login")
    private String emailLogin;

    @Pattern(regexp = Patterns.PASSWORD, message = "*Минимум 8 символов без пробелов")
    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany
    @JoinTable(name = "account_quest",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "quest_id"))
    private Set<Quest> quests = new TreeSet<>();

    public enum Role {
        ROLE_OWNER,
        ROLE_ADMIN,
        ROLE_USER,
    }
}
