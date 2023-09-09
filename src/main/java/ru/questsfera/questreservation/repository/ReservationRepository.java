package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;

import java.time.LocalDate;
import java.util.LinkedList;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    LinkedList<Reservation> findAllByQuestAndDateReserveOrderByTimeReserve(Quest quest, LocalDate date);
    boolean existsByQuest(Quest quest);
    void deleteByQuest(Quest quest);
}