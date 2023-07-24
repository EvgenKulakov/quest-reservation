package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "statuses")
public class StatusesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name_mode")
    private String nameAndMode;

    public StatusesEntity() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameAndMode() {
        return nameAndMode;
    }

    public void setNameAndMode(String nameAndMode) {
        this.nameAndMode = nameAndMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusesEntity that = (StatusesEntity) o;
        return id == that.id && Objects.equals(nameAndMode, that.nameAndMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameAndMode);
    }
}
