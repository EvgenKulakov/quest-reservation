package ru.questsfera.questreservation.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.questsfera.questreservation.model.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByQuestId(Integer questId);

    void deleteByQuestId(Integer questId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reservation r " +
            "WHERE r.questId = :questId " +
            "AND r.dateReserve = :dateReserve " +
            "AND r.timeReserve = :timeReserve " +
            "AND r.statusType != 'CANCEL'")
    boolean existsByQuestIdAndDateReserveAndTimeReserve(Integer questId, LocalDate dateReserve, LocalTime timeReserve);
}