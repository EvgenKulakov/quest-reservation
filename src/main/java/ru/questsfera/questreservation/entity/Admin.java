package ru.questsfera.questreservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import ru.questsfera.questreservation.dto.Account;
import ru.questsfera.questreservation.dto.Role;
import ru.questsfera.questreservation.validator.Patterns;

import java.util.*;

@Entity
@Table(name = "admins", schema = "quest_reservations")
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

    public Admin() {}

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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public Set<Quest> getQuests() {
        return quests;
    }

    public void setQuests(Set<Quest> quests) {
        this.quests = quests;
    }

    public Set<BlackList> getBlackLists() {
        return blackLists;
    }

    public void setBlackLists(Set<BlackList> blackLists) {
        this.blackLists = blackLists;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
