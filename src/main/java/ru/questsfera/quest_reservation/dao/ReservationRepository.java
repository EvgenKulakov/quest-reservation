package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}