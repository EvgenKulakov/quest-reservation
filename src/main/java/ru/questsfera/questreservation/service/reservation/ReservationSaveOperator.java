package ru.questsfera.questreservation.service.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.dto.ResFormDTO;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.Editor;
import ru.questsfera.questreservation.processor.ReservationFactory;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.client.ClientService;
import ru.questsfera.questreservation.service.company.CompanyService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationSaveOperator {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AccountService accountService;

    @Transactional
    public void saveReservation(ResFormDTO resFormDTO, String slotJSON, Principal principal) {
        Account account = accountService.getAccountByLogin(principal.getName());
        Company company = companyService.findById(account.getCompany().getId());
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = null;

        if (slot.getStatusType() == StatusType.EMPTY) {
            reservation = ReservationFactory.createReservation(resFormDTO, slot);
            Client client = new Client(resFormDTO, company);
            reservation.setClient(client);
            client.setReservations(new ArrayList<>(List.of(reservation)));
            reservation.setSourceReserve("default"); //TODO: source reserve
        } else {
            reservation = reservationService.getReserveById(slot.getReservationId());
            Editor.editReservation(reservation, resFormDTO);
        }

        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default"); //TODO: history message

        Client client = reservation.getClient();

        try {
            reservationService.saveReservation(reservation);
            clientService.saveClient(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
