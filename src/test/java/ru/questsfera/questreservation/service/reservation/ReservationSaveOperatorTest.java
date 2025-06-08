package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationSaveOperatorTest {

    @Mock ReservationService reservationService;
    @Mock ClientService clientService;
    @Mock Principal principal;
    @Mock ReservationMapper reservationMapper;
    @Mock SlotJsonMapper slotJsonMapper;
    @InjectMocks ReservationSaveOperator reservationSaveOperator;

    @Test
    void saveUsingResFormAndSlot_saveNew() {
        ReservationWIthClient reservationWIthClient = Mockito.mock(ReservationWIthClient.class);
        Client client = Mockito.mock(Client.class);
        Integer clientId = 1;
        ReservationForm reservationForm = Mockito.mock(ReservationForm.class);
        String slotJson = getSLotEmptyJson();
        Slot slotObject = getEmptySlot();

        when(clientService.saveClient(any(Client.class))).thenReturn(client);
        when(client.getId()).thenReturn(clientId);
        when(slotJsonMapper.toObject(slotJson)).thenReturn(slotObject);
        reservationSaveOperator.saveUsingResFormAndSlot(reservationForm, slotJson, principal);

        verify(clientService).saveClient(any(Client.class));
        verify(reservationService).saveReservation(any(Reservation.class));
        verify(reservationWIthClient, never()).editWithResForm(reservationForm);
    }

    @Test
    void saveUsingResFormAndSlot_saveExists() {
        ReservationWIthClient reservationWIthClient = Mockito.mock(ReservationWIthClient.class);
        Client client = Mockito.mock(Client.class);
        Reservation reservation = Mockito.mock(Reservation.class);
        ReservationForm reservationForm = Mockito.mock(ReservationForm.class);
        String slotJson = getSLotWithReserveJson();
        Slot slotObject = getSLotWithReserve();

        when(reservationService.findReservationWIthClientById(anyLong())).thenReturn(reservationWIthClient);
        when(reservationWIthClient.editWithResForm(reservationForm)).thenReturn(reservationWIthClient);
        when(reservationWIthClient.getClient()).thenReturn(client);
        when(reservationMapper.toEntity(reservationWIthClient)).thenReturn(reservation);
        when(slotJsonMapper.toObject(slotJson)).thenReturn(slotObject);

        reservationSaveOperator.saveUsingResFormAndSlot(reservationForm, slotJson, principal);

        verify(reservationWIthClient).editWithResForm(reservationForm);
        verify(clientService).saveClient(client);
        verify(reservationMapper).toEntity(reservationWIthClient);
        verify(reservationService).saveReservation(reservation);
    }

    @Test
    void saveBlockReservationUsingSlot() {
        String slotJson = getSLotEmptyJson();
        Slot slotObject = getEmptySlot();

        when(slotJsonMapper.toObject(slotJson)).thenReturn(slotObject);
        reservationSaveOperator.saveBlockReservationUsingSlot(slotJson);

        verify(reservationService).saveReservation(any(Reservation.class));
    }

    private String getSLotWithReserveJson() {
        return """
                {
                  "companyId" : 1,
                  "questId" : 1,
                  "questName" : "Quest One",
                  "reservationId" : 3,
                  "date" : "22-04-2025 (вторник)",
                  "time" : "17:00",
                  "price" : 3000,
                  "statuses" : [ {
                    "name" : "NEW_RESERVE",
                    "text" : "Новый"
                  }, {
                    "name" : "CANCEL",
                    "text" : "Отменён"
                  }, {
                    "name" : "CONFIRMED",
                    "text" : "Подтверждён"
                  }, {
                    "name" : "NOT_COME",
                    "text" : "Не пришёл"
                  }, {
                    "name" : "COMPLETED",
                    "text" : "Завершён"
                  } ],
                  "status" : {
                    "name" : "NOT_COME",
                    "text" : "Не пришёл"
                  },
                  "minPersons" : 1,
                  "maxPersons" : 6
                }""";
    }

    private String getSLotEmptyJson() {
        return """
                {
                  "companyId" : 1,
                  "questId" : 1,
                  "questName" : "Quest One",
                  "reservationId" : null,
                  "date" : "22-04-2025 (вторник)",
                  "time" : "17:00",
                  "price" : 3000,
                  "statuses" : [ {
                    "name" : "NEW_RESERVE",
                    "text" : "Новый"
                  }, {
                    "name" : "CANCEL",
                    "text" : "Отменён"
                  }, {
                    "name" : "CONFIRMED",
                    "text" : "Подтверждён"
                  }, {
                    "name" : "NOT_COME",
                    "text" : "Не пришёл"
                  }, {
                    "name" : "COMPLETED",
                    "text" : "Завершён"
                  } ],
                  "status" : {
                    "name" : "NOT_COME",
                    "text" : "Не пришёл"
                  },
                  "minPersons" : 1,
                  "maxPersons" : 6
                }""";
    }

    private Slot getSLotWithReserve() {
        return new Slot(
                1,
                1,
                "Quest One",
                3L,
                LocalDate.of(2025, 4, 22),
                LocalTime.of(17, 0),
                3000,
                Status.DEFAULT_STATUSES,
                Status.NOT_COME,
                1,
                6
        );
    }

    private Slot getEmptySlot() {
        return new Slot(
                1,
                1,
                "Quest One",
                null,
                LocalDate.of(2025, 4, 22),
                LocalTime.of(17, 0),
                3000,
                Status.DEFAULT_STATUSES,
                Status.EMPTY,
                1,
                6
        );
    }
}