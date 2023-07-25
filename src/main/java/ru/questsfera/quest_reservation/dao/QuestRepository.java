package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.entity.QuestEntity;

public interface QuestRepository extends JpaRepository<QuestEntity, Integer> {
}