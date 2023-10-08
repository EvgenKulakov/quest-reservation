package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.Quest;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Integer> {

    boolean existsQuestByQuestNameAndAdmin(String questName, Admin admin);

    boolean existsQuestByIdAndAdmin(Integer id, Admin admin);

    List<Quest> findAllByAdminOrderByQuestName(Admin admin);
}