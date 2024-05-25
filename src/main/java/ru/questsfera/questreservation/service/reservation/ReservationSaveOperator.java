package ru.questsfera.questreservation.service.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.redis.object.AccountRedis;
import ru.questsfera.questreservation.redis.object.ClientRedis;
import ru.questsfera.questreservation.redis.object.ReservationRedis;
import ru.questsfera.questreservation.redis.service.AccountRedisService;
import ru.questsfera.questreservation.redis.service.ClientRedisService;
import ru.questsfera.questreservation.redis.service.ReservationRedisService;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.dto.ReservationForm;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.Editor;
import ru.questsfera.questreservation.processor.ReservationFactory;
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
    private AccountRedisService accountRedisService;
    @Autowired
    private ReservationRedisService reservationRedisService;
    @Autowired
    private ClientRedisService clientRedisService;


    @Transactional
    public void saveReservation(ReservationForm resForm, String slotJSON, Principal principal) {

        AccountRedis accountRedis = accountRedisService.findByEmailLogin(principal.getName());
        Company company = companyService.findById(accountRedis.getCompanyId());
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = null;

        if (slot.getReservation() == null) {
            reservation = ReservationFactory.createReservation(resForm, slot);
            Client client = new Client(resForm, company);
            reservation.setClient(client);
            client.setReservations(new ArrayList<>(List.of(reservation)));
            reservation.setSourceReserve("default"); //TODO: source reserve
        } else {
            reservation = reservationService.getReserveById(slot.getReservation().getId());
            Editor.editReservation(reservation, resForm);
        }

        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default"); //TODO: history message

        Client client = reservation.getClient();

        try {
            reservationService.saveReservation(reservation);
            reservationRedisService.save(new ReservationRedis(reservation));
            clientService.saveClient(client);
            clientRedisService.save(new ClientRedis(client), client.getReservations());
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
        reservationRedisService.save(new ReservationRedis(reservation));
    }
}
