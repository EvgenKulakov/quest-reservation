package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_reserve")
    private LocalDate dateReserve;

    @Column(name = "time_reserve")
    private LocalTime timeReserve;

    @Column(name = "time_created")
    private LocalDateTime dateAndTimeCreated;

    @Column(name = "time_last_change")
    private LocalDateTime timeLastChange;

    @Column(name = "changed_slot_time")
    private LocalTime changedSlotTime;

    @Column(name = "auto_block")
    private LocalTime autoBlock;

    @ManyToOne
    @JoinColumn(name = "quest_id")
    private Quest quest;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "source_reserve")
    private String sourceReserve;

    @Column(name = "changed_price")
    private Integer changedPrice;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "count_persons")
    private int countPersons;

    @Column(name = "admin_comment")
    private String adminComment;

    @Column(name = "client_comment")
    private String clientComment;

    @Column(name = "history_messages")
    private String historyMessages;

    public Reservation() {}

    public Reservation(LocalDate dateReserve, LocalTime timeReserve,
                       LocalDateTime dateAndTimeCreated, Quest quest,
                       Status status, String sourceReserve,
                       int countPersons) {
        this.dateReserve = dateReserve;
        this.timeReserve = timeReserve;
        this.dateAndTimeCreated = dateAndTimeCreated;
        this.quest = quest;
        if (!this.quest.getStatuses().contains(status)) {
            throw new RuntimeException("Попытка создать Бронирование с недоступным стусом");
        }
        this.status = status;
        this.sourceReserve = sourceReserve;
        this.countPersons = countPersons;
        this.historyMessages = "default";
    }

    public Reservation(LocalDate dateReserve, LocalTime timeReserve,
                       LocalDateTime dateAndTimeCreated, Quest quest,
                       Status status, String sourceReserve,
                       int countPersons, Client client) {
        this.dateReserve = dateReserve;
        this.timeReserve = timeReserve;
        this.dateAndTimeCreated = dateAndTimeCreated;
        this.quest = quest;
        if (!this.quest.getStatuses().contains(status)) {
            throw new RuntimeException("Попытка создать Бронирование с недоступным стусом");
        }
        this.status = status;
        this.sourceReserve = sourceReserve;
        this.countPersons = countPersons;
        this.historyMessages = "default";
        this.client = client;
    }

    public void addClient(Client client) {
        client.getReservations().add(this);
        this.client = client;
    }

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

    public LocalDate getDateReserve() {
        return dateReserve;
    }

    public void setDateReserve(LocalDate dateReserve) {
        this.dateReserve = dateReserve;
    }

    public LocalTime getTimeReserve() {
        return timeReserve;
    }

    public void setTimeReserve(LocalTime timeReserve) {
        this.timeReserve = timeReserve;
    }

    public LocalDateTime getDateAndTimeCreated() {
        return dateAndTimeCreated;
    }

    public void setDateAndTimeCreated(LocalDateTime dateAndTimeCreated) {
        this.dateAndTimeCreated = dateAndTimeCreated;
    }

    public LocalDateTime getTimeLastChange() {
        return timeLastChange;
    }

    public void setTimeLastChange(LocalDateTime timeLastChange) {
        this.timeLastChange = timeLastChange;
    }

    public LocalTime getChangedSlotTime() {
        return changedSlotTime;
    }

    public void setChangedSlotTime(LocalTime changedSlotTime) {
        this.changedSlotTime = changedSlotTime;
    }

    public LocalTime getAutoBlock() {
        return autoBlock;
    }

    public void setAutoBlock(LocalTime autoBlock) {
        this.autoBlock = autoBlock;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public String getSourceReserve() {
        return sourceReserve;
    }

    public void setSourceReserve(String sourceReserve) {
        this.sourceReserve = sourceReserve;
    }

    public Integer getChangedPrice() {
        return changedPrice;
    }

    public void setChangedPrice(Integer changedPrice) {
        this.changedPrice = changedPrice;
    }

    public int getCountPersons() {
        return countPersons;
    }

    public void setCountPersons(int countPersons) {
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
