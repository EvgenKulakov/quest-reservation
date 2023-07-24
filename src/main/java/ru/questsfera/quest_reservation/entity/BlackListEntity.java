package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "black_list")
public class BlackListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "messages")
    private String messages;

    public BlackListEntity() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlackListEntity that = (BlackListEntity) o;
        return id == that.id && Objects.equals(phone, that.phone) && Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, messages);
    }
}
