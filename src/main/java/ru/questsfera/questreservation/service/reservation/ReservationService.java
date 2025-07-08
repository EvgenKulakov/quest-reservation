package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.model.dto.ReservationWithClient;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.model.entity.Reservation;
import ru.questsfera.questreservation.repository.jdbc.ReservationJdbcRepository;
import ru.questsfera.questreservation.repository.jpa.ReservationRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationJdbcRepository reservationJdbcRepository;

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'ANY')")
    public ReservationWithClient findReservationWIthClientById(Long id) {
        return reservationJdbcRepository.findReservationWithClientById(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationWithClient> findActiveByQuestIdsAndDate(Collection<Integer> questIds, LocalDate dateReserve) {
        if (questIds.isEmpty()) return Collections.emptyList();
        return reservationJdbcRepository.findActiveByQuestIdsAndDate(questIds, dateReserve);
    }

    @Transactional(readOnly = true)
    public boolean hasReservationsByQuest(Quest quest) {
        return reservationRepository.existsByQuestId(quest.getId());
    }

    @Transactional
    public void saveReservation(Reservation reservation) {
        doubleCheck(reservation);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteBlockedReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    private void doubleCheck(Reservation reservation) {

        if (reservation.getId() == null) {
            boolean existsReservation = reservationRepository.existsByQuestIdAndDateReserveAndTimeReserve(
                    reservation.getQuestId(),
                    reservation.getDateReserve(),
                    reservation.getTimeReserve()
            );

            if (existsReservation) {
                throw new RuntimeException("Double reservations");
            }
        }
    }
}
