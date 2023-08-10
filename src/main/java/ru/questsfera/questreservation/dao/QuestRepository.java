package ru.questsfera.questreservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.Quest;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Integer> {
    List<Quest> findQuestsByAdmin(Admin admin);
//    List<Quest> findQuestsByUsersContains(User user);
}