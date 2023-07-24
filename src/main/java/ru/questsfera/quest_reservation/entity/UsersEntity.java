package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password_crypt")
    private String passwordCrypt;

    @Basic(optional = false)
    @Column(name = "mail")
    private String mail;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminsEntity admin;

    @ManyToMany
    @JoinTable(name = "user_quest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "quest_id"))
    private Set<QuestsEntity> quests = new HashSet<>();

    public UsersEntity() {}

    public Set<QuestsEntity> getQuests() {
        return quests;
    }

    public void setQuests(Set<QuestsEntity> quests) {
        this.quests = quests;
    }

    public AdminsEntity getAdmin() {
        return admin;
    }

    public void setAdmin(AdminsEntity admin) {
        this.admin = admin;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return id == that.id
                && Objects.equals(username, that.username)
                && Objects.equals(passwordCrypt, that.passwordCrypt)
                && Objects.equals(mail, that.mail)
                && Objects.equals(admin, that.admin)
                && Objects.equals(quests, that.quests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, passwordCrypt, mail, admin, quests);
    }
}
