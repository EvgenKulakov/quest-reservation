package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.cache.object.ReservationCache;
import ru.questsfera.questreservation.cache.service.ReservationCacheService;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.CacheCalendar;
import ru.questsfera.questreservation.repository.ClientRepository;
import ru.questsfera.questreservation.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ReservationCacheService reservationCacheService;


    @Transactional
    public Reservation getReserveById(Long id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            return optionalReservation.get();
        }
        throw new RuntimeException("Данного бронирования не существует");
    }

    @Transactional
    public LinkedList<Reservation> getReservationsByDate(Quest quest, LocalDate date) {
        return reservationRepository.findAllByQuestAndDateReserveOrderByTimeReserve(quest, date);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<Reservation> findActiveByDates(List<LocalDate> dates) {

        List<Reservation> reservations = reservationRepository.findAllByDateReserveIn(dates);

        return reservations
                .stream()
                .filter(reservation -> reservation.getStatusType() != StatusType.CANCEL)
                .toList();
    }

    @Transactional
    public List<Reservation> findAllByListId(List<Long> ids) {
        return reservationRepository.findAllByIdIn(ids);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveReservation(Reservation reservation) {
//        checkSecurityForReserve(reservation, account);
        try {
            if (reservation.getClient() != null) {
                clientRepository.save(reservation.getClient());
            }
            reservationRepository.save(reservation);

            ReservationCache reservationCache = new ReservationCache(reservation);
            Date dateOfDeletion = CacheCalendar.getDateOfDeletion(reservationCache.getDateReserve());
            reservationCacheService.save(reservationCache, dateOfDeletion);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }

    @Transactional
    public void deleteBlockedReservation(Reservation reservation) {
//        checkSecurityForReserve(reservation, account);
        reservationRepository.delete(reservation);
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
