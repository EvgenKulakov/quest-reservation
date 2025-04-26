package ru.questsfera.questreservation.repository.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.config.Profile;

import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles(Profile.H2_TEST)
@Sql(scripts = {"classpath:common_test_data.sql"})
public class AccountRepositoryTest {

    static final String ACCOUNT_LOGIN = "admin@gmail.com";
    static final String NOT_EXISTS_LOGIN = "not-exists-login@gmail.com";

    @Autowired
    AccountRepository accountRepository;

    @Test
    void findAccountByLogin_success() {
        Account actualAccount = accountRepository.findAccountByLogin(ACCOUNT_LOGIN).orElseThrow();
        Account exceptedAccount = getAccount();
        assertThat(actualAccount)
                .usingRecursiveComparison()
                .withEqualsForType((q1, q2) -> Set.of(q1).equals(Set.of(q2)), Set.class)
                .isEqualTo(exceptedAccount);
    }

    @Test
    void findAccountByLogin_failure() {
        assertThatThrownBy(() -> accountRepository.findAccountByLogin(NOT_EXISTS_LOGIN).orElseThrow())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findAccountByLoginWithQuests_success() {
        Account actualAccount = accountRepository.findAccountByLoginWithQuests(ACCOUNT_LOGIN).orElseThrow();
        Account exceptedAccount = getAccount();
        assertThat(actualAccount)
                .usingRecursiveComparison()
                .ignoringFields("quests")
                .isEqualTo(exceptedAccount);

        Set<Quest> actualQuests = new TreeSet<>(actualAccount.getQuests());
        Set<Quest> exceptedQuests = exceptedAccount.getQuests();
        assertThat(actualQuests)
                .usingRecursiveComparison()
                .ignoringFields("accounts")
                .isEqualTo(exceptedQuests);
    }

    @Test
    void findAccountByLoginWithQuests_failure() {
        assertThatThrownBy(() -> accountRepository.findAccountByLoginWithQuests(NOT_EXISTS_LOGIN).orElseThrow())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void existsAccountByLogin() {
        boolean existsAccount = accountRepository.existsAccountByLogin(ACCOUNT_LOGIN);
        boolean notExistsAccount = accountRepository.existsAccountByLogin(NOT_EXISTS_LOGIN);
        assertThat(existsAccount).isTrue();
        assertThat(notExistsAccount).isFalse();
    }

    @Test
    void existsAccountByIdAndCompanyId() {
        boolean existsAccount = accountRepository.existsAccountByIdAndCompanyId(1, 1);
        boolean notExistsAccount1 = accountRepository.existsAccountByIdAndCompanyId(100, 1);
        boolean notExistsAccount2 = accountRepository.existsAccountByIdAndCompanyId(1, 100);
        boolean notExistsAccount3 = accountRepository.existsAccountByIdAndCompanyId(100, 100);

        assertThat(existsAccount).isTrue();
        assertThat(notExistsAccount1).isFalse();
        assertThat(notExistsAccount2).isFalse();
        assertThat(notExistsAccount3).isFalse();
    }

    @Test
    void findAllByCompanyId() {
        List<Account> actualAccounts = accountRepository.findAllByCompanyId(1);
        assertThat(actualAccounts.size()).isEqualTo(3);
    }

    @Test
    void findAllByQuestIdOrderByName() {
        List<Account> actualAccounts = accountRepository.findAllByQuestIdOrderByName(1);
        List<Account> exceptedAccounts = getAccountsWithIdAndName();
        assertThat(actualAccounts)
                .usingRecursiveComparison()
                .ignoringFields("login", "password", "phone", "role", "companyId", "quests")
                .isEqualTo(exceptedAccounts);
    }

    private Account getAccount() {
        return Account.builder()
                .id(1)
                .login(ACCOUNT_LOGIN)
                .password("$2a$10$I6WnbfYRb2Z8uBysTKy5l.uSazvJYhqFgsj4LQ.5vZc65TmGlcat6")
                .firstName("Test")
                .lastName("Евгений")
                .phone("+79995554433")
                .role(Account.Role.ROLE_OWNER)
                .companyId(1)
                .quests(getQuestsWithoutAccounts())
                .build();
    }

    private Set<Quest> getQuestsWithoutAccounts() {
        String slotListQuestOne = """
               {
                 "monday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "tuesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "wednesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "thursday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "friday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "saturday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "sunday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ]
               }""";

        String slotListQuestTwo = """
                {
                  "monday" : [ {"time" : "12:30","price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "tuesday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "wednesday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "thursday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "friday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "saturday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "sunday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ]
                }""";

        Quest questOne = Quest.builder()
                .id(1)
                .questName("Quest One")
                .minPersons(1)
                .maxPersons(6)
                .autoBlock(LocalTime.MIN)
                .slotList(slotListQuestOne)
                .companyId(1)
                .statuses("NEW_RESERVE,CANCEL,CONFIRMED,NOT_COME,COMPLETED")
                .synchronizedQuests(new HashSet<>())
                .build();

        Quest questTwo = Quest.builder()
                .id(2)
                .questName("Quest Two")
                .minPersons(1)
                .maxPersons(5)
                .autoBlock(LocalTime.MIN)
                .slotList(slotListQuestTwo)
                .companyId(1)
                .statuses("NEW_RESERVE,CANCEL,CONFIRMED,NOT_COME,COMPLETED")
                .synchronizedQuests(new HashSet<>())
                .build();

        return new TreeSet<>(Set.of(questOne, questTwo));
    }

    private List<Account> getAccountsWithIdAndName() {
        return List.of(
                Account.builder().id(1).firstName("Test").lastName("Евгений").build(),
                Account.builder().id(2).firstName("second").lastName("second").build(),
                Account.builder().id(3).firstName("second").lastName("third").build()
        );
    }
}
