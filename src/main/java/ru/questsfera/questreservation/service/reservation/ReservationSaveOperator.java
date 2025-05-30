package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.mapper.ReservationMapper;
import ru.questsfera.questreservation.mapper.SlotJsonMapper;
import ru.questsfera.questreservation.model.dto.ResFormDTO;
import ru.questsfera.questreservation.model.dto.ReservationWIthClient;
import ru.questsfera.questreservation.model.dto.Slot;
import ru.questsfera.questreservation.model.dto.StatusType;
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
    public void saveUsingResFormAndSlot(ResFormDTO resFormDTO, String slotJSON, Principal principal) {
        Slot slot = SlotJsonMapper.createSlotObject(slotJSON);
        if (slot.getStatusType() == StatusType.EMPTY) {
            saveNewReservation(resFormDTO, slot);
        } else {
            saveExistsReservation(resFormDTO, slot);
        }
    }

    private void saveNewReservation(ResFormDTO resFormDTO, Slot slot) {
        Reservation reservation = Reservation.fromResFormAndSlot(resFormDTO, slot);
        Client newClient = Client.fromResFormAndCompanyId(resFormDTO, slot.getCompanyId());
        Client clientSaved = clientService.saveClient(newClient);

        reservation.setClientId(clientSaved.getId());
        reservation.setSourceReserve("default"); //TODO: source reserve
        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }

    private void saveExistsReservation(ResFormDTO resFormDTO, Slot slot) {
        ReservationWIthClient reservationWIthClient = reservationService.findReservationDtoById(slot.getReservationId());
        ReservationWIthClient editedReservationWIthClient = reservationWIthClient.editWithResForm(resFormDTO);

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
