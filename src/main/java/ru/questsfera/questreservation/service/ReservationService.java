package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.cache.object.ReservationCache;
import ru.questsfera.questreservation.cache.service.ReservationCacheService;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ReservationCacheService reservationCacheService;


    @Transactional
    public Reservation getReserveById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Transactional
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
    public void saveReservation(Reservation reservation) {
//        checkSecurityForReserve(reservation, account);
        try {
            if (reservation.getClient() != null) {
                clientService.save(reservation.getClient());
            }
            reservationRepository.save(reservation);

            ReservationCache reservationCache = new ReservationCache(reservation);
            reservationCacheService.save(reservationCache);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Transactional
    public void deleteBlockedReservation(Long reservationId) {
//        checkSecurityForReserve(reservation, account);
        reservationRepository.deleteById(reservationId);
        reservationCacheService.deleteById(reservationId);
    }

    @Transactional
    public void checkSecurityForReserve(Reservation reservation, Company company) {
        if (!company.getQuests().contains(reservation.getQuest())) {
            throw new SecurityException("Нет доступа для редактирования данного бронирования");
        }
    }

    @Transactional
    public void doubleCheck(Reservation reservation) {

        boolean existsReservation = reservationRepository.existsByQuestAndDateReserveAndTimeReserve(
                reservation.getQuest(),
                reservation.getDateReserve(),
                reservation.getTimeReserve()
        );

        if (existsReservation) {
            throw new RuntimeException("Два бронирования на одно и тоже время");
        }
    }
}
