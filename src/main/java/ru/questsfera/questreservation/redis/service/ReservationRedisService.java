package ru.questsfera.questreservation.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.redis.object.ReservationRedis;
import ru.questsfera.questreservation.redis.repository.ReservationRedisRepository;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.processor.RedisCalendar;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ReservationRedisService {
    @Autowired
    private ReservationRedisRepository reservationRedisRepository;


    public void save(ReservationRedis reservationRedis) {
        if (reservationRedis.getStatusType() != StatusType.CANCEL
                && RedisCalendar.isDateForRedis(reservationRedis.getDateReserve())) {
            reservationRedisRepository.save(reservationRedis);
        }
        else deleteById(reservationRedis.getId());
    }

    public ReservationRedis findByQuestIdDateTime(Integer questId, LocalDate dateReserve, LocalTime timeReserve) {
        return reservationRedisRepository
                .findByQuestIdAndDateReserveAndTimeReserve(questId, dateReserve, timeReserve)
                .orElse(null);
    }

    public void deleteById(Long reservationId) {
        reservationRedisRepository.deleteById(reservationId);
    }
}
