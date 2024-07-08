package ru.questsfera.questreservation.service.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.redis.object.AccountRedis;
import ru.questsfera.questreservation.redis.service.AccountRedisService;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.dto.ReservationForm;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.Editor;
import ru.questsfera.questreservation.processor.ReservationFactory;
import ru.questsfera.questreservation.service.client.ClientService;
import ru.questsfera.questreservation.service.company.CompanyService;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class ReservationSaveOperator {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AccountRedisService accountRedisService;
//    @Autowired
//    private ReservationRedisService reservationRedisService;
//    @Autowired
//    private ClientRedisService clientRedisService;


    @Transactional
    public void saveReservation(ReservationForm resForm, String slotJSON, Principal principal) {

        AccountRedis accountRedis = accountRedisService.findByEmailLogin(principal.getName());
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = null;

        if (slot.getReservation() == null) {
            reservation = ReservationFactory.createReservation(resForm, slot);
            Client newClient = new Client(resForm, accountRedis.getCompanyId());
            Client clientSaved = clientService.saveClient(newClient);
            reservation.setClientId(clientSaved.getId());
            reservation.setSourceReserve("default"); //TODO: source reserve
        } else {
            reservation = reservationService.getReserveById(slot.getReservation().getId());
            Client client = clientService.findById(reservation.getClientId());
            Editor.editReserveAndClient(reservation, client, resForm);
            clientService.saveClient(client);
        }

        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default"); //TODO: history message

        try {
            reservationService.saveReservation(reservation);
//            reservationRedisService.save(new ReservationRedis(reservation));
//            clientRedisService.save(new ClientRedis(client), client.getReservations());
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
//        reservationRedisService.save(new ReservationRedis(reservation));
    }
}
