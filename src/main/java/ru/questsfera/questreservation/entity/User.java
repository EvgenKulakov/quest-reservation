package ru.questsfera.questreservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.dto.Account;
import ru.questsfera.questreservation.dto.Role;
import ru.questsfera.questreservation.validator.Patterns;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "quest_reservations_db")
public class User implements Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Pattern(regexp = Patterns.PASSWORD, message = "*Минимум 8 символов без пробелов")
    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Pattern(regexp = Patterns.EMAIL, message = "*Проверьте правильное написание Email")
    @Column(name = "email")
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_quest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "quest_id"))
    private Set<Quest> quests = new TreeSet<>();

    public User(Admin admin) {
        this.admin = admin;
    }

    public void deleteQuestForUser(Quest quest) {
        this.quests.remove(quest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
