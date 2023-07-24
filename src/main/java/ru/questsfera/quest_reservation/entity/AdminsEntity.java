package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "admins")
public class AdminsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic(optional = false)
    @Column(name = "username")
    private String username;

    @Column(name = "mail")
    private String mail;

    @Basic(optional = false)
    @Column(name = "phone")
    private String phone;

    @Column(name = "password_crypt")
    private String passwordCrypt;

    @Column(name = "money")
    private int money;

    @OneToMany
    @JoinColumn(name = "admin_id")
    private Set<ClientsEntity> clients = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "admin_id")
    private Set<BlackListEntity> blackLists = new HashSet<>();

    @OneToMany(mappedBy = "admin")
    private Set<QuestsEntity> quests = new HashSet<>();

    @OneToMany(mappedBy = "admin")
    private Set<UsersEntity> users = new HashSet<>();

    public AdminsEntity() {}

    public Set<UsersEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UsersEntity> users) {
        this.users = users;
    }

    public Set<QuestsEntity> getQuests() {
        return quests;
    }

    public void setQuests(Set<QuestsEntity> quests) {
        this.quests = quests;
    }

    public Set<BlackListEntity> getBlackLists() {
        return blackLists;
    }

    public void setBlackLists(Set<BlackListEntity> blackLists) {
        this.blackLists = blackLists;
    }

    public Set<ClientsEntity> getClients() {
        return clients;
    }

    public void setClients(Set<ClientsEntity> clients) {
        this.clients = clients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordCrypt() {
        return passwordCrypt;
    }

    public void setPasswordCrypt(String passwordCrypt) {
        this.passwordCrypt = passwordCrypt;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminsEntity that = (AdminsEntity) o;
        return id == that.id
                && money == that.money
                && Objects.equals(username, that.username)
                && Objects.equals(mail, that.mail)
                && Objects.equals(phone, that.phone)
                && Objects.equals(passwordCrypt, that.passwordCrypt)
                && Objects.equals(clients, that.clients)
                && Objects.equals(blackLists, that.blackLists)
                && Objects.equals(quests, that.quests)
                && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, mail, phone, passwordCrypt, money, clients, blackLists, quests, users);
    }
}
