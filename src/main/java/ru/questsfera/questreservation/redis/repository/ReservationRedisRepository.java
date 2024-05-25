package ru.questsfera.questreservation.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.redis.object.ReservationRedis;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface ReservationRedisRepository extends CrudRepository<ReservationRedis, Long> {

    Optional<ReservationRedis> findByQuestIdAndDateReserveAndTimeReserve(
            Integer questId,
            LocalDate dateReserve,
            LocalTime timeReserve
    );
}
