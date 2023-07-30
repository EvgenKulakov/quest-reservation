package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.entity.Quest;
import ru.questsfera.quest_reservation.entity.Reservation;

import java.sql.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAllByQuestAndDateReserve(Quest quest, Date date);
}