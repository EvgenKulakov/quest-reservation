package ru.questsfera.questreservation.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.questsfera.questreservation.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findAccountByLogin(String login);

    @Query("SELECT ac FROM Account ac LEFT JOIN FETCH ac.quests WHERE ac.login = :login")
    Optional<Account> findAccountByLoginWithQuests(@Param("login") String login);

    boolean existsAccountByLogin(String login);

    boolean existsAccountByIdAndCompanyId(Integer accountId, Integer companyId);

    List<Account> findAllByCompanyId(Integer companyId);

    @Query("SELECT ac FROM Account ac " +
            "JOIN ac.quests qu " +
            "WHERE qu.id = :questId " +
            "ORDER BY ac.firstName, ac.lastName")
    List<Account> findAllByQuestIdOrderByName(@Param("questId") Integer questId);
}
