package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "admins")
public class AdminEntity {

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
    private List<ClientEntity> clients = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "admin_id")
    private List<BlackListEntity> blackLists = new ArrayList<>();

    @OneToMany(mappedBy = "admin")
    private List<QuestEntity> quests = new ArrayList<>();

    @OneToMany(mappedBy = "admin")
    private List<UserEntity> users = new ArrayList<>();

    public AdminEntity() {}

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public List<QuestEntity> getQuests() {
        return quests;
    }

    public void setQuests(List<QuestEntity> quests) {
        this.quests = quests;
    }

    public List<BlackListEntity> getBlackLists() {
        return blackLists;
    }

    public void setBlackLists(List<BlackListEntity> blackLists) {
        this.blackLists = blackLists;
    }

    public List<ClientEntity> getClients() {
        return clients;
    }

    public void setClients(List<ClientEntity> clients) {
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
        AdminEntity that = (AdminEntity) o;
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
