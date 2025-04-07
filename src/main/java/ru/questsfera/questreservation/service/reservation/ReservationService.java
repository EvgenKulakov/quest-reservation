package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.dto.ReservationDTO;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.repository.ReservationJdbcRepository;
import ru.questsfera.questreservation.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationJdbcRepository reservationJdbcRepository;

    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public ReservationDTO findReservationDtoById(Long id) {
        return reservationJdbcRepository.findReservationDtoById(id);
    }

    @Transactional(readOnly = true)
    public Map<LocalTime, Reservation> findActiveByQuestIdAndDate(Integer questId, LocalDate date) {

        Map<LocalTime, Reservation> reservationMap = new HashMap<>();
        List<Reservation> reservations = reservationRepository.findAllByQuestIdAndDateReserve(questId, date);

        for (Reservation reservation : reservations) {
            if (reservation.getStatusType() != StatusType.CANCEL) {
                if (!reservationMap.containsKey(reservation.getTimeReserve())) {
                    reservationMap.put(reservation.getTimeReserve(), reservation);
                }
                else throw new RuntimeException("Double reservation");
            }
        }

        return reservationMap;
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findActiveByQuestIdsAndDate(List<Integer> questIds, LocalDate dateReserve) {
        return reservationJdbcRepository.findActiveByQuestIdsAndDate(questIds, dateReserve);
    }

    @Transactional
    public List<Reservation> findActiveByClientId(Integer clientId) {
        return reservationRepository.findAllByClientId(clientId)
                .stream()
                .filter(reservation -> reservation.getStatusType() != StatusType.CANCEL)
                .toList();
    }

    @Transactional
    public List<Reservation> findActiveByDates(List<LocalDate> dates) {

        List<Reservation> reservations = reservationRepository.findAllByDateReserveIn(dates);

        return reservations
                .stream()
                .filter(reservation -> reservation.getStatusType() != StatusType.CANCEL)
                .toList();
    }

    @Transactional
    public List<Reservation> findActiveByDate(LocalDate dateReserve) {

        List<Reservation> reservations = reservationRepository.findAllByDateReserve(dateReserve);

        return reservations
                .stream()
                .filter(reservation -> reservation.getStatusType() != StatusType.CANCEL)
                .toList();
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

    @Transactional
    public void checkSecurityForReserve(Reservation reservation, Company company) {
//        if (!company.getQuests().contains(reservation.getQuest())) {
//            throw new SecurityException("Нет доступа для редактирования данного бронирования");
//        }
    }

    @Transactional
    public void doubleCheck(Reservation reservation) {

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
