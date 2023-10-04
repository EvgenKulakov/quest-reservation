package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.Quest;

public interface QuestRepository extends JpaRepository<Quest, Integer> {

    boolean existsQuestByQuestNameAndAdmin(String questName, Admin admin);
}