package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "reservations")
public class ReservationsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_reserve")
    private Date dateReserve;

    @Column(name = "time_reserve")
    private Time timeReserve;

    @Basic(optional = false)
    @Column(name = "time_created")
    private Timestamp timeCreated;

    @Basic(optional = false)
    @Column(name = "time_last_change")
    private Timestamp timeLastChange;

    @Basic(optional = false)
    @Column(name = "changed_slot_time")
    private Time changedSlotTime;

    @Basic(optional = false)
    @Column(name = "auto_block")
    private Time autoBlock;

    @Column(name = "quest_name")
    private String questName;

    @OneToOne
    @JoinColumn(name = "status_id")
    private StatusesEntity status;

    @Column(name = "sourse_reserve")
    private String sourseReserve;

    @Basic(optional = false)
    @Column(name = "changed_price")
    private Integer changedPrice;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientsEntity client;

    @Basic(optional = false)
    @Column(name = "count_persons")
    private Byte countPersons;

    @Basic(optional = false)
    @Column(name = "admin_comment")
    private String adminComment;

    @Basic(optional = false)
    @Column(name = "client_comment")
    private String clientComment;

    @Column(name = "history_messages")
    private String historyMessages;

    public ReservationsEntity() {}

    public StatusesEntity getStatus() {
        return status;
    }

    public void setStatus(StatusesEntity status) {
        this.status = status;
    }

    public ClientsEntity getClient() {
        return client;
    }

    public void setClient(ClientsEntity client) {
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateReserve() {
        return dateReserve;
    }

    public void setDateReserve(Date dateReserve) {
        this.dateReserve = dateReserve;
    }

    public Time getTimeReserve() {
        return timeReserve;
    }

    public void setTimeReserve(Time timeReserve) {
        this.timeReserve = timeReserve;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Timestamp getTimeLastChange() {
        return timeLastChange;
    }

    public void setTimeLastChange(Timestamp timeLastChange) {
        this.timeLastChange = timeLastChange;
    }

    public Time getChangedSlotTime() {
        return changedSlotTime;
    }

    public void setChangedSlotTime(Time changedSlotTime) {
        this.changedSlotTime = changedSlotTime;
    }

    public Time getAutoBlock() {
        return autoBlock;
    }

    public void setAutoBlock(Time autoBlock) {
        this.autoBlock = autoBlock;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String getSourseReserve() {
        return sourseReserve;
    }

    public void setSourseReserve(String sourseReserve) {
        this.sourseReserve = sourseReserve;
    }

    public Integer getChangedPrice() {
        return changedPrice;
    }

    public void setChangedPrice(Integer changedPrice) {
        this.changedPrice = changedPrice;
    }

    public Byte getCountPersons() {
        return countPersons;
    }

    public void setCountPersons(Byte countPersons) {
        this.countPersons = countPersons;
    }

    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }

    public String getClientComment() {
        return clientComment;
    }

    public void setClientComment(String clientComment) {
        this.clientComment = clientComment;
    }

    public String getHistoryMessages() {
        return historyMessages;
    }

    public void setHistoryMessages(String historyMessages) {
        this.historyMessages = historyMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationsEntity that = (ReservationsEntity) o;
        return id == that.id
                && Objects.equals(dateReserve, that.dateReserve)
                && Objects.equals(timeReserve, that.timeReserve)
                && Objects.equals(timeCreated, that.timeCreated)
                && Objects.equals(timeLastChange, that.timeLastChange)
                && Objects.equals(changedSlotTime, that.changedSlotTime)
                && Objects.equals(autoBlock, that.autoBlock)
                && Objects.equals(questName, that.questName)
                && Objects.equals(status, that.status)
                && Objects.equals(sourseReserve, that.sourseReserve)
                && Objects.equals(changedPrice, that.changedPrice)
                && Objects.equals(client, that.client)
                && Objects.equals(countPersons, that.countPersons)
                && Objects.equals(adminComment, that.adminComment)
                && Objects.equals(clientComment, that.clientComment)
                && Objects.equals(historyMessages, that.historyMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateReserve, timeReserve, timeCreated, timeLastChange,
                changedSlotTime, autoBlock, questName, status, sourseReserve, changedPrice,
                client, countPersons, adminComment, clientComment, historyMessages);
    }
}
