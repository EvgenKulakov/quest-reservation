package ru.questsfera.questreservation.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.cache.object.ReservationCache;
import ru.questsfera.questreservation.cache.repository.ReservationCacheRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ReservationCacheService {
    @Autowired
    private ReservationCacheRepository reservationCacheRepository;


    public void save(ReservationCache reservationCache) {
        reservationCacheRepository.save(reservationCache);
    }

    public ReservationCache findByQuestIdDateTime(Integer questId, LocalDate dateReserve, LocalTime timeReserve) {
        return reservationCacheRepository
                .findByQuestIdAndDateReserveAndTimeReserve(questId, dateReserve, timeReserve)
                .orElse(null);
    }

    public void delete(ReservationCache reservationCache) {
        reservationCacheRepository.delete(reservationCache);
    }
}
