package ru.questsfera.questreservation.cache.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.cache.object.ReservationCache;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface ReservationCacheRepository extends CrudRepository<ReservationCache, Integer> {

    Optional<ReservationCache> findByQuestIdAndDateReserveAndTimeReserve(
            Integer questId,
            LocalDate dateReserve,
            LocalTime timeReserve
    );
}
