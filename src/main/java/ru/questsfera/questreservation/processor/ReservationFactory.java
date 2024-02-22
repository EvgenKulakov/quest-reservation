package ru.questsfera.questreservation.processor;

import ru.questsfera.questreservation.dto.ReservationForm;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Reservation;

import java.time.LocalDateTime;

public class ReservationFactory {

    public static Reservation createReservation(ReservationForm resForm, Slot slot) {
        Reservation reservation = new Reservation();
        reservation.setDateReserve(slot.getDate());
        reservation.setTimeReserve(slot.getTime());
        reservation.setDateAndTimeCreated(LocalDateTime.now());
        reservation.setQuest(slot.getQuest());
        reservation.setStatusType(resForm.getStatusType());
        reservation.setCountPersons(resForm.getCountPersons());
        reservation.setAdminComment(resForm.getAdminComment());
        reservation.setClientComment(resForm.getClientComment());
        return reservation;
    }

    public static Reservation createBlockReservation(Slot slot) {
        Reservation reservation = new Reservation();
        reservation.setDateReserve(slot.getDate());
        reservation.setTimeReserve(slot.getTime());
        reservation.setDateAndTimeCreated(LocalDateTime.now());
        reservation.setQuest(slot.getQuest());
        reservation.setStatusType(StatusType.BLOCK);
        return reservation;
    }
}
