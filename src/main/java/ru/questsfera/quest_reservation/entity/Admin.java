package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "admins", schema = "quest_reservations")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "mail")
    private String mail;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password_crypt")
    private String passwordCrypt;

    @Column(name = "money")
    private int money;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.REFRESH)
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "admin")
    private Set<BlackList> blackLists = new HashSet<>();

    @OneToMany(mappedBy = "admin")
    private Set<Quest> quests = new HashSet<>();

    @OneToMany(mappedBy = "admin",
            cascade = {CascadeType.REMOVE})
    private Set<User> users = new HashSet<>();

    public Admin() {}

    public Admin(String mail, String passwordCrypt) {
        this.username = "User";
        this.mail = mail;
        this.passwordCrypt = passwordCrypt;
        this.money = 0;
    }

    public void addUserForAdmin(User user) {
        if (user.getAdmin() != null) {
            if(!user.getAdmin().equals(this)) {
                throw  new RuntimeException("Юзеру " + user.getId()
                        + " уже установлен админ id: " + user.getAdmin().getId());
            }
        }
        user.setAdmin(this);
        this.users.add(user);
    }

    public void deleteUserForAdmin(User user) {
        this.users.remove(user);
    }

    public void addQuestForAdmin(Quest quest) {
        if (quest.getAdmin() != null) {
            if(!quest.getAdmin().equals(this)) {
                throw  new RuntimeException("Квесту " + quest.getId()
                        + " уже установлен админ id: " + quest.getAdmin().getId());
            } else return;
        }
        quest.setAdmin(this);
        this.quests.add(quest);
    }

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
        if (!(o instanceof Admin admin)) return false;
        return id != null && Objects.equals(getId(), admin.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
