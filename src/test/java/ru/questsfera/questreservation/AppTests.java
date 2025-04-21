package ru.questsfera.questreservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.repository.jdbc.AccountJdbcRepository;
import ru.questsfera.questreservation.repository.jdbc.ReservationJdbcRepository;
import ru.questsfera.questreservation.repository.jpa.AccountRepository;
import ru.questsfera.questreservation.repository.jpa.ReservationRepository;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.quest.QuestService;
import ru.questsfera.questreservation.service.reservation.ReservationGetOperator;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@SpringBootTest
@ActiveProfiles(TestProfiles.H2)
@Sql(scripts = {"classpath:common_test_data.sql"})
class AppTests {
    @Autowired
    AccountService accountService;
    @Autowired
    QuestService questService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReservationJdbcRepository reservationJdbcRepository;
    @Autowired
    ReservationGetOperator reservationGetOperator;
    @Autowired
    AccountJdbcRepository accountJdbcRepository;
    @Autowired
    AccountRepository accountRepository;

    @Mock
    Principal principal;

    @BeforeEach
    void setUp() {
        Mockito.when(principal.getName()).thenReturn("admin@yandex.ru");
    }

    @Test
//    @Transactional
    void hotTest() {
        List<Integer> questsIds = List.of(3, 4);
        String accountName = "admin@yandex.ru";
        LocalDate date = LocalDate.of(2025, 4, 7);

//        Account account = accountService.getAccountByLogin(accountName);
        Account account = accountRepository.findAccountByLoginWithQuests(accountName).get();
        Set<Quest> quests = account.getQuests();
        System.out.println(quests);

        System.out.println(account);

    }
}
