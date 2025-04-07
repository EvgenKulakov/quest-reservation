package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Quest;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Integer> {

    boolean existsQuestByQuestNameAndCompany(String questName, Company company);

    boolean existsQuestByIdAndCompany(Integer id, Company company);

    List<Quest> findAllByCompanyOrderByQuestName(Company company);

    @Query("SELECT qu FROM Quest qu JOIN qu.accounts ac WHERE ac.id = :accountId")
    List<Quest> findAllByAccountId(@Param("accountId") Integer accountId);

    @Query("SELECT qu FROM Quest qu JOIN qu.accounts ac WHERE ac.login = :login")
    List<Quest> findAllByAccount_login(String login);
}