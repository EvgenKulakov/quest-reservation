package ru.questsfera.questreservation.entity;

import jakarta.persistence.*;

import ru.questsfera.questreservation.dto.ReservationForm;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.StatusType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "reservations", schema = "quest_reservations")
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

    @Enumerated(value = EnumType.STRING)
    @JoinColumn(name = "status_type")
    private StatusType statusType;

    @Column(name = "source_reserve")
    private String sourceReserve;

    @Column(name = "changed_price")
    private Integer changedPrice;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.MERGE, CascadeType.DETACH})
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

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public Reservation(ReservationForm resForm, Slot slot) {
        this.dateReserve = slot.getDate();
        this.timeReserve = slot.getTime();
        this.dateAndTimeCreated = LocalDateTime.now();
        this.quest = slot.getQuest();
        this.statusType = resForm.getStatusType();
        this.countPersons = resForm.getCountPersons();
        this.adminComment = resForm.getAdminComment();
        this.clientComment = resForm.getClientComment();
        this.admin = slot.getQuest().getAdmin();
    }

    public Reservation() {}

    public static Reservation createBlockReservation(Slot slot) {
        Reservation reservation = new Reservation();
        reservation.dateReserve = slot.getDate();
        reservation.timeReserve = slot.getTime();
        reservation.dateAndTimeCreated = LocalDateTime.now();
        reservation.quest = slot.getQuest();
        reservation.statusType = StatusType.BLOCK;
        reservation.admin = slot.getQuest().getAdmin();
        return reservation;
    }

    public void addClient(Client client) {
        client.getReservations().add(this);
        this.client = client;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
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
