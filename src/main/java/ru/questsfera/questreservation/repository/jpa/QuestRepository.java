package ru.questsfera.questreservation.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.questsfera.questreservation.entity.Quest;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Integer> {

    boolean existsQuestByQuestNameAndCompanyId(String questName, Integer companyId);

    boolean existsQuestByIdAndCompanyId(Integer id, Integer companyId);

    List<Quest> findAllByCompanyIdOrderByQuestName(Integer companyId);

    @Query("SELECT qu FROM Quest qu LEFT JOIN FETCH qu.accounts ac WHERE ac.id = :accountId")
    List<Quest> findAllByAccountId(@Param("accountId") Integer accountId);

    @Query("SELECT qu FROM Quest qu JOIN FETCH qu.accounts ac WHERE ac.login = :login")
    List<Quest> findAllByAccount_login(String login);
}