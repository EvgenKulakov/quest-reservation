package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.mapper.ReservationMapper;
import ru.questsfera.questreservation.model.dto.ResFormDTO;
import ru.questsfera.questreservation.model.dto.ReservationDTO;
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
        ReservationDTO reservationDTO = Mockito.mock(ReservationDTO.class);
        Client client = Mockito.mock(Client.class);
        Integer clientId = 1;
        ResFormDTO resFormDTO = Mockito.mock(ResFormDTO.class);
        String slotJson = getSLotEmptyJson();

        when(clientService.saveClient(any(Client.class))).thenReturn(client);
        when(client.getId()).thenReturn(clientId);
        reservationSaveOperator.saveUsingResFormAndSlot(resFormDTO, slotJson, principal);

        verify(clientService).saveClient(any(Client.class));
        verify(reservationService).saveReservation(any(Reservation.class));
        verify(reservationDTO, never()).editWithResForm(resFormDTO);
    }

    @Test
    void saveUsingResFormAndSlot_saveExists() {
        ReservationDTO reservationDTO = Mockito.mock(ReservationDTO.class);
        Client client = Mockito.mock(Client.class);
        Reservation reservation = Mockito.mock(Reservation.class);
        ResFormDTO resFormDTO = Mockito.mock(ResFormDTO.class);
        String slotJson = getSLotWithReserveJson();

        when(reservationService.findReservationDtoById(anyLong())).thenReturn(reservationDTO);
        when(reservationDTO.editWithResForm(resFormDTO)).thenReturn(reservationDTO);
        when(reservationDTO.getClient()).thenReturn(client);
        when(reservationMapper.toEntity(reservationDTO)).thenReturn(reservation);

        reservationSaveOperator.saveUsingResFormAndSlot(resFormDTO, slotJson, principal);

        verify(reservationDTO).editWithResForm(resFormDTO);
        verify(clientService).saveClient(client);
        verify(reservationMapper).toEntity(reservationDTO);
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
                    "type" : "NEW_RESERVE",
                    "text" : "Новый"
                  }, {
                    "id" : 2,
                    "type" : "CANCEL",
                    "text" : "Отменён"
                  }, {
                    "id" : 3,
                    "type" : "CONFIRMED",
                    "text" : "Подтверждён"
                  }, {
                    "id" : 4,
                    "type" : "NOT_COME",
                    "text" : "Не пришёл"
                  }, {
                    "id" : 5,
                    "type" : "COMPLETED",
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
                    "type" : "NEW_RESERVE",
                    "text" : "Новый"
                  }, {
                    "id" : 2,
                    "type" : "CANCEL",
                    "text" : "Отменён"
                  }, {
                    "id" : 3,
                    "type" : "CONFIRMED",
                    "text" : "Подтверждён"
                  }, {
                    "id" : 4,
                    "type" : "NOT_COME",
                    "text" : "Не пришёл"
                  }, {
                    "id" : 5,
                    "type" : "COMPLETED",
                    "text" : "Завершён"
                  } ],
                  "statusType" : "EMPTY",
                  "minPersons" : 1,
                  "maxPersons" : 6
                }""";
    }
}