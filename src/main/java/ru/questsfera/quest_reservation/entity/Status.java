package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;
import ru.questsfera.quest_reservation.processor.StatusType;

import java.util.*;

@Entity
@Table(name = "statuses", schema = "quest_reservations")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "status_type")
    @Enumerated(value = EnumType.STRING)
    private StatusType type;

    @ManyToMany(mappedBy = "statuses")
    private Set<Quest> quests = new HashSet<>();

    public Status() {}

    public Status(StatusType type) {
        this.type = type;
    }

    public Status(String name, StatusType type) {
        this.name = name;
        this.type = type;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StatusType getType() {
        return type;
    }

    public void setType(StatusType type) {
        this.type = type;
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
