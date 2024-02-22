package ru.questsfera.questreservation.processor;

import ru.questsfera.questreservation.dto.ReservationForm;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Reservation;

import java.util.Collections;

public class Editor {

    public static void editReservation(Reservation reservation, ReservationForm resForm) {
        Client client = reservation.getClient();

        reservation.setStatusType(resForm.getStatusType());
        reservation.setCountPersons(resForm.getCountPersons());
        reservation.setAdminComment(resForm.getAdminComment());
        reservation.setClientComment(resForm.getClientComment());

        client.setFirstName(resForm.getFirstName());
        client.setLastName(resForm.getLastName());
        client.setPhones(resForm.getPhone());
        client.setEmails(resForm.getEmail());
    }
}
