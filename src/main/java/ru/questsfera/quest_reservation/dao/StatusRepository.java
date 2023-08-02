package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.model.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}