package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "statuses")
public class StatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name_mode")
    private String nameAndMode;

    @ManyToMany
    @JoinTable(name = "status_quest",
            joinColumns = @JoinColumn(name = "status_id"),
            inverseJoinColumns = @JoinColumn(name = "quest_id"))
    private List<QuestEntity> quests = new ArrayList<>();

    public StatusEntity() {}

    public List<QuestEntity> getQuests() {
        return quests;
    }

    public void setQuests(List<QuestEntity> quests) {
        this.quests = quests;
    }

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
        StatusEntity that = (StatusEntity) o;
        return id == that.id && Objects.equals(nameAndMode, that.nameAndMode) && Objects.equals(quests, that.quests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameAndMode, quests);
    }
}
