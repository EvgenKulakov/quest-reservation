package ru.questsfera.questreservation.processor;

import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.dto.ResFormDTO;
import ru.questsfera.questreservation.dto.ReservationDTO;

@Component
public class Editor {

    public void editReservationAndClient(ReservationDTO reservationDTO, ResFormDTO resForm) {
        reservationDTO.setStatusType(resForm.getStatusType());
        reservationDTO.setCountPersons(resForm.getCountPersons());
        reservationDTO.setAdminComment(resForm.getAdminComment());
        reservationDTO.setClientComment(resForm.getClientComment());

        reservationDTO.getClient().setFirstName(resForm.getFirstName());
        reservationDTO.getClient().setLastName(resForm.getLastName());
        reservationDTO.getClient().setPhones(resForm.getPhone());
        reservationDTO.getClient().setEmails(resForm.getEmail());
    }
}
