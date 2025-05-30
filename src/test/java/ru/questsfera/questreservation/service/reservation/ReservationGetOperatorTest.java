package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.model.dto.ReservationWIthClient;
import ru.questsfera.questreservation.model.dto.Slot;
import ru.questsfera.questreservation.model.dto.SlotListPage;
import ru.questsfera.questreservation.model.dto.Status;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.service.quest.QuestService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationGetOperatorTest {

    @Mock ReservationService reservationService;
    @Mock QuestService questService;
    @Mock Principal principal;
    @InjectMocks ReservationGetOperator reservationGetOperator;

    LocalDate date;
    String accountLogin;
    Set<Quest> quests;

    @BeforeEach
    void setUp() {
        date = LocalDate.now();
        accountLogin = "login";
        quests = Set.of(getQuest());

        when(principal.getName()).thenReturn(accountLogin);
        when(questService.findAllByAccount_login(accountLogin)).thenReturn(quests);
    }

    @Test
    void getQuestsAndSlotsByDate_success() {
        List<ReservationWIthClient> reservationDTOs = List.of(getResWithClient());
        when(reservationService.findActiveByQuestIdsAndDate(anyList(), any(LocalDate.class))).thenReturn(reservationDTOs);

        SlotListPage actualSlotListPage = reservationGetOperator.getQuestsAndSlotsByDate(date, principal);
        SlotListPage exceptedSlotListPage = new SlotListPage(getQuestNamesAndSlots(), getUseStatuses());

        assertThat(actualSlotListPage)
                .usingRecursiveComparison()
                .isEqualTo(exceptedSlotListPage);

        verify(questService).findAllByAccount_login(accountLogin);
        verify(reservationService).findActiveByQuestIdsAndDate(anyList(), any(LocalDate.class));
    }

    @Test
    void getQuestsAndSlotsByDate_doubleBlockingFailure() {
        List<ReservationWIthClient> reservationsWithClient = List.of(getResWithClient(), getResWithClient());
        when(reservationService.findActiveByQuestIdsAndDate(anyList(), any(LocalDate.class))).thenReturn(reservationsWithClient);

        assertThatThrownBy(() -> reservationGetOperator.getQuestsAndSlotsByDate(date, principal))
                .isInstanceOf(RuntimeException.class)
                .hasMessageStartingWith("Double reservation");
    }

    private Quest getQuest() {
        String slotListQuestOne = """
               {
                 "monday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "tuesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "wednesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "thursday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "friday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "saturday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "sunday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ]
               }""";

        return Quest.builder()
                .id(1)
                .questName("Quest One")
                .minPersons(1)
                .maxPersons(6)
                .autoBlock(LocalTime.MIN)
                .slotList(slotListQuestOne)
                .companyId(1)
                .statuses(Status.DEFAULT_STATUSES)
                .synchronizedQuests(new HashSet<>())
                .build();
    }

    private ReservationWIthClient getResWithClient() {
        return ReservationWIthClient.builder()
                .id(1L)
                .timeReserve(LocalTime.parse("12:00:00"))
                .questId(1)
                .price(new BigDecimal(3000))
                .status(Status.CONFIRMED)
                .build();
    }

    private Map<String, List<Slot>> getQuestNamesAndSlots() {
        Slot slot1 = Slot.fromQuestDateReservationPrice(getQuest(), LocalDate.now(), getResWithClient(), 3000);
        Slot slot2 = Slot.emptyFromQuestDateTimePrice(getQuest(), LocalDate.now(), LocalTime.parse("13:00:00"), 3000);
        return Map.of("Quest One", List.of(slot1, slot2));
    }

    private Set<Status> getUseStatuses() {
        return Set.of(Status.CONFIRMED);
    }
}