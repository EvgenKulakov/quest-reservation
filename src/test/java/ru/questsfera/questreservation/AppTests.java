package ru.questsfera.questreservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.questsfera.questreservation.dto.ReservationDTO;
import ru.questsfera.questreservation.repository.ReservationJdbcRepository;
import ru.questsfera.questreservation.repository.ReservationRepository;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.quest.QuestService;
import ru.questsfera.questreservation.service.reservation.ReservationGetOperator;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;


@SpringBootTest
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

    @Mock
    Principal principal;

    @BeforeEach
    void setUp() {
        Mockito.when(principal.getName()).thenReturn("admin@yandex.ru");
    }

    @Test
    void hotTest() {
        List<Integer> questsIds = List.of(3, 2);
        String accountName = "admin@yandex.ru";
        LocalDate date = LocalDate.of(2025, 4, 7);

        List<ReservationDTO> excFromJdbc =
                reservationJdbcRepository.findActiveByQuestIdsAndDate(questsIds, date);

//        ReservationDTO reservationDTO = reservationJdbcRepository.findReservationDtoById(1L);
//        System.out.println(reservationDTO);

//        SlotListPageDTO questsAndSlotsByDate = reservationGetOperator.getQuestsAndSlotsByDate(date, principal);

        System.out.println(excFromJdbc);

    }
}
