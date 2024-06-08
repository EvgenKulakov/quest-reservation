package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Company;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findAccountByEmailLogin(String emailLogin);

    boolean existsAccountByEmailLogin(String emailLogin);

    boolean existsAccountByIdAndCompanyId(Integer accountd, Integer companyId);

    List<Account> findAllByCompanyIdOrderByEmailLogin(Integer companyId);

    @Query("SELECT ac FROM Account ac JOIN ac.quests qu WHERE qu.id = :questId")
    List<Account> findAllByQuestId(@Param("questId") Integer questId);
}
