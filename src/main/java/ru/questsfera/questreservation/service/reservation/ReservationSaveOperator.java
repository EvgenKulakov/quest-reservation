package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.converter.ReservationMapper;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.dto.ResFormDTO;
import ru.questsfera.questreservation.dto.ReservationDTO;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.ReservationFactory;
import ru.questsfera.questreservation.service.client.ClientService;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationSaveOperator {

    private final ReservationService reservationService;
    private final ClientService clientService;
    private final ReservationFactory reservationFactory;
    private final ReservationMapper reservationMapper;

    @Transactional
    public void saveReservation(ResFormDTO resFormDTO, String slotJSON, Principal principal) {
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = null;

        if (slot.getStatusType() == StatusType.EMPTY) {
            reservation = reservationFactory.createReservation(resFormDTO, slot);
            Client newClient = new Client(resFormDTO, slot.getCompanyId());
            Client clientSaved = clientService.saveClient(newClient);
            reservation.setClientId(clientSaved.getId());
            reservation.setSourceReserve("default"); //TODO: source reserve
        } else {
            ReservationDTO reservationDTO = reservationService.findReservationDtoById(slot.getReservationId());
            ReservationDTO editedReservationDTO = reservationDTO.editUsingResForm(resFormDTO);
            clientService.saveClient(editedReservationDTO.getClient());
            reservation = reservationMapper.toEntity(editedReservationDTO);
        }

        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }

    @Transactional
    public void saveBlockReservation(String slotJSON) {
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = reservationFactory.createBlockReservation(slot);

        reservation.setSourceReserve("default"); //TODO: source reserve
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }
}
