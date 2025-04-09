package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.dto.ResFormDTO;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.Editor;
import ru.questsfera.questreservation.processor.ReservationFactory;
import ru.questsfera.questreservation.service.client.ClientService;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationSaveOperator {

    private final ReservationService reservationService;
    private final ClientService clientService;

    @Transactional
    public void saveReservation(ResFormDTO resFormDTO, String slotJSON, Principal principal) {
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = null;

        if (slot.getStatusType() == StatusType.EMPTY) {
            reservation = ReservationFactory.createReservation(resFormDTO, slot);
            Client newClient = new Client(resFormDTO, slot.getCompanyId());
            Client clientSaved = clientService.saveClient(newClient);
            reservation.setClientId(clientSaved.getId());
            reservation.setSourceReserve("default"); //TODO: source reserve
        } else {
            reservation = reservationService.findById(slot.getReservationId());
            Client client = clientService.findById(reservation.getClientId());
            Editor.editReservationAndClient(reservation, client, resFormDTO);
            clientService.saveClient(client);
        }

        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }

    @Transactional
    public void saveBlockReservation(String slotJSON) {
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = ReservationFactory.createBlockReservation(slot);

        reservation.setSourceReserve("default"); //TODO: source reserve
        reservation.setHistoryMessages("default"); //TODO: history message

        reservationService.saveReservation(reservation);
    }
}
