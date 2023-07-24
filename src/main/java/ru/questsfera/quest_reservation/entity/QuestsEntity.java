package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.sql.Time;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "quests")
public class QuestsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "quest_name")
    private String questName;

    @Column(name = "slot_list")
    private String slotList;

    @Column(name = "min_persons")
    private byte minPersons;

    @Column(name = "max_persons")
    private byte maxPersons;

    @Basic(optional = false)
    @Column(name = "auto_block")
    private Time autoBlock;

    @Basic(optional = false)
    @Column(name = "sms")
    private String sms;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminsEntity admin;

    @ManyToMany
    @JoinTable(name = "status_quest",
            joinColumns = @JoinColumn(name = "quest_id"),
            inverseJoinColumns = @JoinColumn(name = "status_id"))
    private Set<StatusesEntity> statuses = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "synchronized_quests",
            joinColumns = @JoinColumn(name = "id_first_quest"),
            inverseJoinColumns = @JoinColumn(name = "id_second_quest"))
    private Set<QuestsEntity> synchronizedQuests = new HashSet<>();

    public QuestsEntity() {}

    public Set<QuestsEntity> getSynchronizedQuests() {
        return synchronizedQuests;
    }

    public void setSynchronizedQuests(Set<QuestsEntity> synchronizedQuests) {
        this.synchronizedQuests = synchronizedQuests;
    }

    public Set<StatusesEntity> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<StatusesEntity> statuses) {
        this.statuses = statuses;
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

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String getSlotList() {
        return slotList;
    }

    public void setSlotList(String slotList) {
        this.slotList = slotList;
    }

    public byte getMinPersons() {
        return minPersons;
    }

    public void setMinPersons(byte minPersons) {
        this.minPersons = minPersons;
    }

    public byte getMaxPersons() {
        return maxPersons;
    }

    public void setMaxPersons(byte maxPersons) {
        this.maxPersons = maxPersons;
    }

    public Time getAutoBlock() {
        return autoBlock;
    }

    public void setAutoBlock(Time autoBlock) {
        this.autoBlock = autoBlock;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestsEntity that = (QuestsEntity) o;
        return id == that.id
                && minPersons == that.minPersons
                && maxPersons == that.maxPersons
                && Objects.equals(questName, that.questName)
                && Objects.equals(slotList, that.slotList)
                && Objects.equals(autoBlock, that.autoBlock)
                && Objects.equals(sms, that.sms)
                && Objects.equals(admin, that.admin)
                && Objects.equals(statuses, that.statuses)
                && Objects.equals(synchronizedQuests, that.synchronizedQuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questName, slotList, minPersons, maxPersons,
                autoBlock, sms, admin, statuses, synchronizedQuests);
    }
}
