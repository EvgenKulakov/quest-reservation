package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.mapper.ReservationMapper;
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
    @Mock ReservationMapper reservationMapper;
    @InjectMocks ReservationSaveOperator reservationSaveOperator;

    @Test
    void saveUsingResFormAndSlot_saveNew() {
        ReservationWIthClient reservationWIthClient = Mockito.mock(ReservationWIthClient.class);
        Client client = Mockito.mock(Client.class);
        Integer clientId = 1;
        ReservationForm reservationForm = Mockito.mock(ReservationForm.class);
        Slot slotObject = getEmptySlot();

        when(clientService.saveClient(any(Client.class))).thenReturn(client);
        when(client.getId()).thenReturn(clientId);
        reservationSaveOperator.saveUsingResFormAndSlot(reservationForm, slotObject);

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
        Slot slotObject = getSLotWithReserve();

        when(reservationService.findReservationWIthClientById(anyLong())).thenReturn(reservationWIthClient);
        when(reservationWIthClient.editWithResForm(reservationForm)).thenReturn(reservationWIthClient);
        when(reservationWIthClient.getClient()).thenReturn(client);
        when(reservationMapper.toEntity(reservationWIthClient)).thenReturn(reservation);

        reservationSaveOperator.saveUsingResFormAndSlot(reservationForm, slotObject);

        verify(reservationWIthClient).editWithResForm(reservationForm);
        verify(clientService).saveClient(client);
        verify(reservationMapper).toEntity(reservationWIthClient);
        verify(reservationService).saveReservation(reservation);
    }

    @Test
    void saveBlockReservationUsingSlot() {
        Slot slotObject = getEmptySlot();
        reservationSaveOperator.saveBlockReservationUsingSlot(slotObject);
        verify(reservationService).saveReservation(any(Reservation.class));
    }

    private Slot getSLotWithReserve() {
        return new Slot(
                1,
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