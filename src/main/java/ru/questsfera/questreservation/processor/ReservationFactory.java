package ru.questsfera.questreservation.processor;

import ru.questsfera.questreservation.dto.ResFormDTO;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationFactory {

    public static Reservation createReservation(ResFormDTO resFormDTO, Slot slot) {
        Reservation reservation = new Reservation();
        reservation.setDateReserve(slot.getDate());
        reservation.setTimeReserve(slot.getTime());
        reservation.setDateAndTimeCreated(LocalDateTime.now());
        reservation.setQuestId(slot.getQuestId());
        reservation.setStatusType(resFormDTO.getStatusType());
        reservation.setPrice(new BigDecimal(slot.getPrice()));
        reservation.setCountPersons(resFormDTO.getCountPersons());
        reservation.setAdminComment(resFormDTO.getAdminComment());
        reservation.setClientComment(resFormDTO.getClientComment());
        return reservation;
    }

    public static Reservation createBlockReservation(Slot slot) {
        Reservation reservation = new Reservation();
        reservation.setDateReserve(slot.getDate());
        reservation.setTimeReserve(slot.getTime());
        reservation.setDateAndTimeCreated(LocalDateTime.now());
        reservation.setQuestId(slot.getQuestId());
        reservation.setStatusType(StatusType.BLOCK);
        return reservation;
    }

    public static ResFormDTO createResFormDTO(Reservation reservation, Client client) {
        ResFormDTO resFormDTO = new ResFormDTO();
        resFormDTO.setId(reservation.getId());
        resFormDTO.setStatusType(reservation.getStatusType());
        resFormDTO.setFirstName(client.getFirstName());
        resFormDTO.setLastName(client.getLastName());
        resFormDTO.setPhone(client.getPhones());
        resFormDTO.setEmail(client.getEmails());
        resFormDTO.setCountPersons(reservation.getCountPersons());
        resFormDTO.setAdminComment(reservation.getAdminComment());
        resFormDTO.setClientComment(reservation.getClientComment());
        return resFormDTO;
    }
}
