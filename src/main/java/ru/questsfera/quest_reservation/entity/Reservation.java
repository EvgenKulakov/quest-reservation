package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_reserve")
    private Date dateReserve;

    @Column(name = "time_reserve")
    private Time timeReserve;

    @Column(name = "time_created")
    private Timestamp timeCreated;

    @Column(name = "time_last_change")
    private Timestamp timeLastChange;

    @Column(name = "changed_slot_time")
    private Time changedSlotTime;

    @Column(name = "auto_block")
    private Time autoBlock;

    @Column(name = "quest_name")
    private String questName;

    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "sourse_reserve")
    private String sourseReserve;

    @Column(name = "changed_price")
    private Integer changedPrice;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "count_persons")
    private Byte countPersons;

    @Column(name = "admin_comment")
    private String adminComment;

    @Column(name = "client_comment")
    private String clientComment;

    @Column(name = "history_messages")
    private String historyMessages;

    public Reservation() {}

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        if (!(o instanceof Reservation that)) return false;
        return id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
