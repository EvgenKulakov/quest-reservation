package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.entity.Admin;
import ru.questsfera.quest_reservation.entity.Quest;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Integer> {
    List<Quest> findQuestsByAdmin(Admin admin);
//    List<Quest> findQuestsByUsersContains(User user);
}