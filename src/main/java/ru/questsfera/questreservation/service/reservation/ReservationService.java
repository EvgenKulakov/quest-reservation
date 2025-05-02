package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.dto.ReservationDTO;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
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
    public ReservationDTO findReservationDtoById(Long id) {
        return reservationJdbcRepository.findReservationDtoById(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findActiveByQuestIdsAndDate(Collection<Integer> questIds, LocalDate dateReserve) {
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
//        checkSecurityForReserve(reservation, account);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteBlockedReservation(Long reservationId) {
//        checkSecurityForReserve(reservation, account);
        reservationRepository.deleteById(reservationId);
    }

    // TODO security
    public void checkSecurityForReserve(Reservation reservation, Company company) {
//        if (!company.getQuests().contains(reservation.getQuest())) {
//            throw new SecurityException("Нет доступа для редактирования данного бронирования");
//        }
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
