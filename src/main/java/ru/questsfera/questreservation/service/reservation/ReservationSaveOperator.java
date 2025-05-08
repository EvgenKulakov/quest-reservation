package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.mapper.ReservationMapper;
import ru.questsfera.questreservation.mapper.SlotMapper;
import ru.questsfera.questreservation.model.dto.ResFormDTO;
import ru.questsfera.questreservation.model.dto.ReservationDTO;
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
        Slot slot = SlotMapper.createSlotObject(slotJSON);
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
        ReservationDTO reservationDTO = reservationService.findReservationDtoById(slot.getReservationId());
        ReservationDTO editedReservationDTO = reservationDTO.editWithResForm(resFormDTO);

        clientService.saveClient(editedReservationDTO.getClient());

        Reservation reservation = reservationMapper.toEntity(editedReservationDTO);
        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }

    @Transactional
    public void saveBlockReservationUsingSlot(String slotJSON) {
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = Reservation.blockReservationFromSlot(slot);

        reservation.setSourceReserve("default"); //TODO: source reserve
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }
}
