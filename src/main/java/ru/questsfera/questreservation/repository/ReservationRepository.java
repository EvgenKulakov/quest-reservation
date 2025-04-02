package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.questsfera.questreservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByQuestIdAndDateReserve(Integer questId, LocalDate date);

    boolean existsByQuestId(Integer questId);

    void deleteByQuestId(Integer questId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reservation r " +
            "WHERE r.questId = :questId " +
            "AND r.dateReserve = :dateReserve " +
            "AND r.timeReserve = :timeReserve " +
            "AND r.statusType != 'CANCEL'")
    boolean existsByQuestIdAndDateReserveAndTimeReserve(Integer questId, LocalDate dateReserve, LocalTime timeReserve);

//    List<Reservation> findAllByQuestAndDateReserveIn(Quest quest, List<LocalDate> dates);

    List<Reservation> findAllByIdIn(List<Long> ids);

    List<Reservation> findAllByDateReserveIn(List<LocalDate> dates);

    List<Reservation> findAllByDateReserve(LocalDate dateReserve);

    List<Reservation> findAllByClientId(Integer clientId);
}