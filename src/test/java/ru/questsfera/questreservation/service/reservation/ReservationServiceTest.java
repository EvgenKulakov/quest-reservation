package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.repository.jdbc.ReservationJdbcRepository;
import ru.questsfera.questreservation.repository.jpa.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock ReservationRepository reservationRepository;
    @Mock ReservationJdbcRepository reservationJdbcRepository;
    @InjectMocks ReservationService reservationService;

    @Test
    void findReservationDtoById() {
        reservationService.findReservationDtoById(anyLong());
        verify(reservationJdbcRepository).findReservationDtoById(anyLong());
    }

    @Test
    void findActiveByQuestIdsAndDate_returnData() {
        reservationService.findActiveByQuestIdsAndDate(List.of(1,2), LocalDate.now());
        verify(reservationJdbcRepository).findActiveByQuestIdsAndDate(anyList(), any(LocalDate.class));
    }

    @Test
    void findActiveByQuestIdsAndDate_returnEmpty() {
        reservationService.findActiveByQuestIdsAndDate(Collections.emptyList(), LocalDate.now());
        verify(reservationJdbcRepository, never()).findActiveByQuestIdsAndDate(anyList(), any(LocalDate.class));
    }

    @Test
    void hasReservationsByQuest() {
        Quest quest = Mockito.mock(Quest.class);
        Integer questId = 1;

        when(quest.getId()).thenReturn(questId);
        reservationService.hasReservationsByQuest(quest);

        verify(reservationRepository).existsByQuestId(questId);
    }

    @Test
    void saveReservation_success() {
        Reservation reservation = Mockito.mock(Reservation.class);
        Long reservationId = 1L;

        when(reservation.getId()).thenReturn(reservationId);
        reservationService.saveReservation(reservation);

        verify(reservationRepository).save(reservation);
    }

    @Test
    void saveReservation_doubleCheckFailure() {
        Reservation reservation = Mockito.mock(Reservation.class);
        Integer questId = 1;
        LocalDate dateReserve = LocalDate.now();
        LocalTime timeReserve = LocalTime.now();

        when(reservation.getId()).thenReturn(null);
        when(reservation.getQuestId()).thenReturn(questId);
        when(reservation.getDateReserve()).thenReturn(dateReserve);
        when(reservation.getTimeReserve()).thenReturn(timeReserve);

        when(reservationRepository.existsByQuestIdAndDateReserveAndTimeReserve(questId,
                dateReserve, timeReserve)).thenReturn(Boolean.TRUE);

        assertThatThrownBy(() -> reservationService.saveReservation(reservation))
                .isInstanceOf(RuntimeException.class)
                .hasMessageStartingWith("Double reservations");
    }

    @Test
    void deleteBlockedReservation() {
        reservationService.deleteBlockedReservation(anyLong());
        verify(reservationRepository).deleteById(anyLong());
    }
}