package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.util.*;


@Entity
@Table(name = "statuses")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_mode")
    private String nameAndMode;

    @ManyToMany(mappedBy = "statuses")
    private Set<Quest> quests = new HashSet<>();

    public Status() {}

    public Status(String nameAndMode) {
        this.nameAndMode = nameAndMode;
    }

    public Set<Quest> getQuests() {
        return quests;
    }

    public void setQuests(Set<Quest> quests) {
        this.quests = quests;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        if (!(o instanceof Status status)) return false;
        return id != null && Objects.equals(getId(), status.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
