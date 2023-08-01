package ru.questsfera.quest_reservation.processor;

import ru.questsfera.quest_reservation.entity.Quest;
import ru.questsfera.quest_reservation.entity.Reservation;
import ru.questsfera.quest_reservation.entity.Status;

import java.time.LocalDate;
import java.time.LocalTime;

public class Slot {
    private final Quest quest;
    private final Status status;
    private final Reservation reservation;
    private final LocalDate date;
    private final LocalTime time;
    private final int price;
    private final LocalTime autoBlock;

    public Slot(Quest quest, Status status, Reservation reservation,
                LocalDate date, LocalTime time, int price, LocalTime autoBlock) {
        this.quest = quest;
        this.status = status;
        this.reservation = reservation;
        this.date = date;
        this.time = time;
        this.price = price;
        this.autoBlock = autoBlock;
    }

    public Quest getQuest() {
        return quest;
    }

    public Status getStatus() {
        return status;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getPrice() {
        return price;
    }

    public LocalTime getAutoBlock() {
        return autoBlock;
    }
}
