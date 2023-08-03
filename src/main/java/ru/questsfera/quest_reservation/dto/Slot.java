package ru.questsfera.quest_reservation.dto;

import ru.questsfera.quest_reservation.entity.Status;
import ru.questsfera.quest_reservation.entity.Quest;
import ru.questsfera.quest_reservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Slot {
    private Quest quest;
    private Status status;
    private Reservation reservation;
    private LocalDate date;
    private LocalTime time;
    private Integer price;
    private LocalTime autoBlock;

    public Slot(Quest quest, Status status, Reservation reservation,
                LocalDate date, LocalTime time, Integer price, LocalTime autoBlock) {
        this.quest = quest;
        this.status = status;
        this.reservation = reservation;
        this.date = date;
        this.time = time;
        this.price = price;
        this.autoBlock = autoBlock;
    }

    public Slot() {
    }


    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalTime getAutoBlock() {
        return autoBlock;
    }

    public void setAutoBlock(LocalTime autoBlock) {
        this.autoBlock = autoBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(quest, slot.quest)
                && Objects.equals(date, slot.date)
                && Objects.equals(time, slot.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quest, date, time);
    }
}
