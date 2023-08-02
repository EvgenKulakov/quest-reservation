package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.model.entity.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Integer> {
}