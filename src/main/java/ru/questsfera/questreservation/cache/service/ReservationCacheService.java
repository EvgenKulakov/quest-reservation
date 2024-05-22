package ru.questsfera.questreservation.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.cache.object.ReservationCache;
import ru.questsfera.questreservation.cache.repository.ReservationCacheRepository;
import ru.questsfera.questreservation.dto.StatusType;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ReservationCacheService {
    @Autowired
    private ReservationCacheRepository reservationCacheRepository;


    public void save(ReservationCache reservationCache) {
        if (reservationCache.getStatusType() != StatusType.CANCEL) {
            reservationCacheRepository.save(reservationCache);
        }
        else deleteById(reservationCache.getId());
    }

    public ReservationCache findByQuestIdDateTime(Integer questId, LocalDate dateReserve, LocalTime timeReserve) {
        return reservationCacheRepository
                .findByQuestIdAndDateReserveAndTimeReserve(questId, dateReserve, timeReserve)
                .orElse(null);
    }

    public void deleteById(Long reservationId) {
        reservationCacheRepository.deleteById(reservationId);
    }
}
