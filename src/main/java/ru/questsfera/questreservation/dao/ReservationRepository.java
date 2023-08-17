package ru.questsfera.questreservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    LinkedList<Reservation> findAllByQuestAndDateReserveOrderByTimeReserve(Quest quest, LocalDate date);
    List<Reservation> findAllByQuest(Quest quest);
}