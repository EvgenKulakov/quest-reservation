package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.entity.StatusEntity;

public interface StatusRepository extends JpaRepository<StatusEntity, Integer> {
}