package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.questsfera.questreservation.entity.Quest;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Integer> {

    boolean existsQuestByQuestNameAndCompanyId(String questName, Integer companyId);

    @Query("""
            SELECT CASE WHEN COUNT(q) > 0
                    THEN TRUE ELSE FALSE END
                    FROM Account a JOIN a.quests q WHERE q.id = :id AND a.id = :accountId
            """)
    boolean existsQuestByIdAndAccountId(@Param("id") Integer id, @Param("accountId") Integer accountId);

    List<Quest> findAllByCompanyIdOrderByQuestName(Integer companyId);

    @Query("SELECT q FROM Account a JOIN a.quests q WHERE a.id = :accountId")
    List<Quest> findAllByAccountId(@Param("accountId") Integer accountId);
}