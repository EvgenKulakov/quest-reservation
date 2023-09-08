package ru.questsfera.questreservation.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "users", schema = "quest_reservations")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password_crypt")
    private String passwordCrypt;

    @Column(name = "mail")
    private String mail;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToMany
    @JoinTable(name = "user_quest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "quest_id"))
    private Set<Quest> quests = new HashSet<>();

    public User() {}

    public void addQuestForUser(Quest quest) {
        if (!quest.getAdmin().equals(this.admin)) {
            throw new RuntimeException(
                    "Quest id: " + quest.getId() + " нет доступа для добавления");
        }
        quests.add(quest);
        quest.getUsers().add(this);
    }

    public void deleteQuestForUser(Quest quest) {
        quests.remove(quest);
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
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

    public String getPasswordCrypt() {
        return passwordCrypt;
    }

    public void setPasswordCrypt(String passwordCrypt) {
        this.passwordCrypt = passwordCrypt;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Set<Quest> getQuests() {
        return quests;
    }

    public void setQuests(Set<Quest> quests) {
        this.quests = quests;
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
