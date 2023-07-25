package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.entity.BlackListEntity;

public interface BlackListRepository extends JpaRepository<BlackListEntity, Integer> {
}