package ru.questsfera.questreservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "admins", schema = "quest_reservations_db")
@JsonIgnoreProperties({"username", "mail", "phone", "password",
        "money", "role", "clients", "blackLists", "quests", "users"})
public class Admin implements Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    @Pattern(regexp = Patterns.EMAIL, message = "Проверьте правильное написание email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    @Pattern(regexp = Patterns.PASSWORD, message = "Пароль минимум 8 символов без пробелов")
    private String password;

    @Column(name = "money")
    private int money;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_ADMIN;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.REFRESH)
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "admin")
    private Set<BlackList> blackLists = new HashSet<>();

    @OneToMany(mappedBy = "admin", fetch = FetchType.EAGER)
    private Set<Quest> quests = new TreeSet<>();

    @OneToMany(mappedBy = "admin",
            cascade = {CascadeType.REMOVE})
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "admin")
    private Set<Reservation> reservations = new HashSet<>();

    public void deleteQuestForAdmin(Quest quest) {
        this.quests.remove(quest);
    }

    public void addBlackListForAdmin(BlackList blackList) {
        blackList.setAdmin(this);
        this.blackLists.add(blackList);
    }

    public void deleteBlackListForAdmin(BlackList blackList) {
        this.blackLists.remove(blackList);
    }

    @Override
    public Admin getAdmin() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin admin)) return false;
        return id != null && Objects.equals(getId(), admin.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
