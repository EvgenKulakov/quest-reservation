package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.mapper.ReservationMapper;
import ru.questsfera.questreservation.model.dto.ResFormDTO;
import ru.questsfera.questreservation.model.dto.ReservationWIthClient;
import ru.questsfera.questreservation.model.entity.Client;
import ru.questsfera.questreservation.model.entity.Reservation;
import ru.questsfera.questreservation.service.client.ClientService;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationSaveOperatorTest {

    @Mock ReservationService reservationService;
    @Mock ClientService clientService;
    @Mock Principal principal;
    @Mock ReservationMapper reservationMapper;
    @InjectMocks ReservationSaveOperator reservationSaveOperator;

    @Test
    void saveUsingResFormAndSlot_saveNew() {
        ReservationWIthClient reservationWIthClient = Mockito.mock(ReservationWIthClient.class);
        Client client = Mockito.mock(Client.class);
        Integer clientId = 1;
        ResFormDTO resFormDTO = Mockito.mock(ResFormDTO.class);
        String slotJson = getSLotEmptyJson();

        when(clientService.saveClient(any(Client.class))).thenReturn(client);
        when(client.getId()).thenReturn(clientId);
        reservationSaveOperator.saveUsingResFormAndSlot(resFormDTO, slotJson, principal);

        verify(clientService).saveClient(any(Client.class));
        verify(reservationService).saveReservation(any(Reservation.class));
        verify(reservationWIthClient, never()).editWithResForm(resFormDTO);
    }

    @Test
    void saveUsingResFormAndSlot_saveExists() {
        ReservationWIthClient reservationWIthClient = Mockito.mock(ReservationWIthClient.class);
        Client client = Mockito.mock(Client.class);
        Reservation reservation = Mockito.mock(Reservation.class);
        ResFormDTO resFormDTO = Mockito.mock(ResFormDTO.class);
        String slotJson = getSLotWithReserveJson();

        when(reservationService.findReservationDtoById(anyLong())).thenReturn(reservationWIthClient);
        when(reservationWIthClient.editWithResForm(resFormDTO)).thenReturn(reservationWIthClient);
        when(reservationWIthClient.getClient()).thenReturn(client);
        when(reservationMapper.toEntity(reservationWIthClient)).thenReturn(reservation);

        reservationSaveOperator.saveUsingResFormAndSlot(resFormDTO, slotJson, principal);

        verify(reservationWIthClient).editWithResForm(resFormDTO);
        verify(clientService).saveClient(client);
        verify(reservationMapper).toEntity(reservationWIthClient);
        verify(reservationService).saveReservation(reservation);
    }

    @Test
    void saveBlockReservationUsingSlot() {
        String slotJson = getSLotEmptyJson();
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
                    "id" : 1,
                    "name" : "NEW_RESERVE",
                    "text" : "Новый"
                  }, {
                    "id" : 2,
                    "name" : "CANCEL",
                    "text" : "Отменён"
                  }, {
                    "id" : 3,
                    "name" : "CONFIRMED",
                    "text" : "Подтверждён"
                  }, {
                    "id" : 4,
                    "name" : "NOT_COME",
                    "text" : "Не пришёл"
                  }, {
                    "id" : 5,
                    "name" : "COMPLETED",
                    "text" : "Завершён"
                  } ],
                  "statusType" : "NOT_COME",
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
                    "id" : 1,
                    "name" : "NEW_RESERVE",
                    "text" : "Новый"
                  }, {
                    "id" : 2,
                    "name" : "CANCEL",
                    "text" : "Отменён"
                  }, {
                    "id" : 3,
                    "name" : "CONFIRMED",
                    "text" : "Подтверждён"
                  }, {
                    "id" : 4,
                    "name" : "NOT_COME",
                    "text" : "Не пришёл"
                  }, {
                    "id" : 5,
                    "name" : "COMPLETED",
                    "text" : "Завершён"
                  } ],
                  "statusType" : "EMPTY",
                  "minPersons" : 1,
                  "maxPersons" : 6
                }""";
    }
}