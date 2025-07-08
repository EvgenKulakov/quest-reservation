package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.mapper.ReservationMapper;
import ru.questsfera.questreservation.model.dto.ReservationForm;
import ru.questsfera.questreservation.model.dto.ReservationWithClient;
import ru.questsfera.questreservation.model.dto.Slot;
import ru.questsfera.questreservation.model.dto.Status;
import ru.questsfera.questreservation.model.entity.Client;
import ru.questsfera.questreservation.model.entity.Reservation;
import ru.questsfera.questreservation.service.client.ClientService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationSaveOperator {

    private final ReservationService reservationService;
    private final ClientService clientService;
    private final ReservationMapper reservationMapper;

    @Transactional
    public void saveUsingResFormAndSlot(ReservationForm reservationForm, Slot slot) {
        if (slot.getStatus() == Status.EMPTY) {
            saveNewReservation(reservationForm, slot);
        } else {
            saveExistsReservation(reservationForm, slot);
        }
    }

    private void saveNewReservation(ReservationForm reservationForm, Slot slot) {
        Reservation reservation = Reservation.fromResFormAndSlot(reservationForm, slot);
        Client newClient = Client.fromResFormAndCompanyId(reservationForm, slot.getCompanyId());
        Client clientSaved = clientService.saveClient(newClient);

        reservation.setClientId(clientSaved.getId());
        reservation.setSourceReserve("default"); //TODO: source reserve
        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }

    private void saveExistsReservation(ReservationForm reservationForm, Slot slot) {
        ReservationWithClient reservationWIthClient = reservationService.findReservationWIthClientById(slot.getReservationId());
        ReservationWithClient editedReservationWithClient = reservationWIthClient.editWithResForm(reservationForm);

        clientService.saveClient(editedReservationWithClient.getClient());

        Reservation reservation = reservationMapper.toEntity(editedReservationWithClient);
        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }

    @Transactional
    public void saveBlockReservationUsingSlot(Slot slot) {
        Reservation reservation = Reservation.blockReservationFromSlot(slot);

        reservation.setSourceReserve("default"); //TODO: source reserve
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }
}
