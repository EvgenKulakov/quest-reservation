package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.entity.Quest;
import ru.questsfera.quest_reservation.entity.Reservation;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    LinkedList<Reservation> findAllByQuestAndDateReserve(Quest quest, LocalDate date);
    List<Reservation> findAllByQuest(Quest quest);
}