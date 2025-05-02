package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.converter.ReservationMapper;
import ru.questsfera.questreservation.dto.ResFormDTO;
import ru.questsfera.questreservation.dto.ReservationDTO;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.service.client.ClientService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
        Client clientSaved = Client.builder().id(1).build();
        ResFormDTO resFormDTO = getResFormDto();
        String slotJson = getSLotEmptyJson();

        when(clientService.saveClient(any(Client.class))).thenReturn(clientSaved);
        reservationSaveOperator.saveUsingResFormAndSlot(resFormDTO, slotJson, principal);

        verify(clientService).saveClient(any(Client.class));
        verify(reservationService).saveReservation(any(Reservation.class));
        verify(reservationDTO, never()).editUsingResForm(resFormDTO);
    }

    @Test
    void saveUsingResFormAndSlot_saveExists() {
        ReservationDTO reservationDTO = Mockito.spy(getReservationDto());
        Reservation reservation = getReservation();
        ResFormDTO resFormDTO = getResFormDto();
        String slotJson = getSLotWithReserveJson();

        when(reservationService.findReservationDtoById(anyLong())).thenReturn(reservationDTO);
        when(reservationMapper.toEntity(reservationDTO)).thenReturn(reservation);

        reservationSaveOperator.saveUsingResFormAndSlot(resFormDTO, slotJson, principal);

        verify(reservationDTO).editUsingResForm(resFormDTO);
        verify(clientService).saveClient(reservationDTO.getClient());
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

    private ResFormDTO getResFormDto() {
        return new ResFormDTO(
                3L,
                StatusType.CONFIRMED,
                "Egor",
                "Jukov",
                "+79998887766",
                "some@gmail.com",
                5,
                "admin comment",
                null
        );
    }

    private ReservationDTO getReservationDto() {
        return new ReservationDTO(
                3L,
                LocalDate.parse("2025-04-25"),
                LocalTime.parse("17:00:00"),
                LocalDateTime.parse("2025-04-26T00:58:38.605035"),
                null,
                null,
                1,
                StatusType.NOT_COME,
                "default",
                new BigDecimal(3000),
                null,
                getClient(),
                4,
                null,
                null,
                "default"
        );
    }

    private Client getClient() {
        return new Client(
                1,
                "Egorka",
                "Jukof",
                "+79998887766",
                "ee@email.com",
                null,
                null,
                1
        );
    }

    private Reservation getReservation() {
        return new Reservation(
                3L,
                LocalDate.parse("2025-04-25"),
                LocalTime.parse("17:00:00"),
                LocalDateTime.parse("2025-04-26T00:58:38.605035"),
                null,
                null,
                1,
                StatusType.NOT_COME,
                "default",
                new BigDecimal(3000),
                null,
                1,
                4,
                null,
                null,
                "default"
        );
    }
}