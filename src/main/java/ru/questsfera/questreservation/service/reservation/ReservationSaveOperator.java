package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.mapper.ReservationMapper;
import ru.questsfera.questreservation.mapper.SlotJsonMapper;
import ru.questsfera.questreservation.model.dto.ReservationForm;
import ru.questsfera.questreservation.model.dto.ReservationWIthClient;
import ru.questsfera.questreservation.model.dto.Slot;
import ru.questsfera.questreservation.model.dto.Status;
import ru.questsfera.questreservation.model.entity.Client;
import ru.questsfera.questreservation.model.entity.Reservation;
import ru.questsfera.questreservation.service.client.ClientService;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationSaveOperator {

    private final ReservationService reservationService;
    private final ClientService clientService;
    private final ReservationMapper reservationMapper;

    @Transactional
    public void saveUsingResFormAndSlot(ReservationForm reservationForm, String slotJSON, Principal principal) {
        Slot slot = SlotJsonMapper.createSlotObject(slotJSON);
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
        ReservationWIthClient reservationWIthClient = reservationService.findReservationWIthClientById(slot.getReservationId());
        ReservationWIthClient editedReservationWIthClient = reservationWIthClient.editWithResForm(reservationForm);

        clientService.saveClient(editedReservationWIthClient.getClient());

        Reservation reservation = reservationMapper.toEntity(editedReservationWIthClient);
        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }

    @Transactional
    public void saveBlockReservationUsingSlot(String slotJSON) {
        Slot slot = SlotJsonMapper.createSlotObject(slotJSON);
        Reservation reservation = Reservation.blockReservationFromSlot(slot);

        reservation.setSourceReserve("default"); //TODO: source reserve
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }
}
