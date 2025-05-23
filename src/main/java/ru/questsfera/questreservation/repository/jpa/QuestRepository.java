package ru.questsfera.questreservation.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.questsfera.questreservation.model.entity.Quest;

import java.util.List;
import java.util.Set;

public interface QuestRepository extends JpaRepository<Quest, Integer> {

    boolean existsQuestByQuestNameAndCompanyId(String questName, Integer companyId);

    boolean existsQuestByIdAndCompanyId(Integer id, Integer companyId);

    List<Quest> findAllByCompanyIdOrderByQuestName(Integer companyId);

    @Query("SELECT qu FROM Quest qu JOIN qu.accounts ac WHERE ac.login = :login")
    Set<Quest> findAllByAccount_login(String login);
}