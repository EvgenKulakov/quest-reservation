package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "black_list")
public class BlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "messages")
    private String messages;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public BlackList() {}

    public BlackList(String phone, String messages) {
        this.phone = phone;
        this.messages = messages;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlackList blackList)) return false;
        return id != null && Objects.equals(getId(), blackList.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
