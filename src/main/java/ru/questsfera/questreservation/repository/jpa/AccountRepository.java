package ru.questsfera.questreservation.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.questsfera.questreservation.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findAccountByLogin(String login);

    boolean existsAccountByLogin(String login);

    boolean existsAccountByIdAndCompanyId(Integer accountId, Integer companyId);

    List<Account> findAllByCompanyIdOrderByLogin(Integer companyId);

    @Query("SELECT ac FROM Account ac JOIN ac.quests qu WHERE qu.id = :questId")
    List<Account> findAllByQuestId(@Param("questId") Integer questId);
}
