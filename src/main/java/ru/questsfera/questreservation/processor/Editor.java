package ru.questsfera.questreservation.processor;

import ru.questsfera.questreservation.dto.ReservationForm;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Reservation;

public class Editor {

    public static void editReserveAndClient(Reservation reservation, Client client, ReservationForm resForm) {

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
