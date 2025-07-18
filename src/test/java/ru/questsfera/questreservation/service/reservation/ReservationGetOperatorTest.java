package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.mapper.SlotListJsonMapper;
import ru.questsfera.questreservation.model.dto.*;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.processor.SlotFactory;
import ru.questsfera.questreservation.security.AccountUserDetails;
import ru.questsfera.questreservation.service.quest.QuestService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationGetOperatorTest {

    @Mock ReservationService reservationService;
    @Mock QuestService questService;
    @Mock AccountUserDetails principal;
    @Mock SlotListJsonMapper slotListJsonMapper;
    @Mock SlotFactory slotFactory;
    @InjectMocks ReservationGetOperator reservationGetOperator;

    LocalDate date;
    Integer accountId;
    Set<Quest> quests;

    @BeforeEach
    void setUp() {
        date = LocalDate.now();
        accountId = 1;
        quests = Set.of(getQuest());

        when(principal.getId()).thenReturn(accountId);
        when(questService.findAllByAccount_id(accountId)).thenReturn(quests);
    }

    @Test
    void getQuestsAndSlotsByDate_success() {
        List<ReservationWithClient> reservationDTOs = List.of(getResWithClient());
        List<Slot> slots = getSlots();
        String slotListJson = getSlotListJson();
        SlotList slotListObject = getSLotList();

        when(reservationService.findActiveByQuestIdsAndDate(anyList(), any(LocalDate.class))).thenReturn(reservationDTOs);
        when(slotListJsonMapper.toObject(slotListJson)).thenReturn(slotListObject);
        when(slotFactory.getSlots(any(Quest.class), any(LocalDate.class), any(SlotList.class), anyMap())).thenReturn(slots);

        SlotListPage actualSlotListPage = reservationGetOperator.getQuestsAndSlotsByDate(date, principal);
        SlotListPage exceptedSlotListPage = new SlotListPage(getQuestNamesAndSlots(), getUseStatuses());

        assertThat(actualSlotListPage)
                .usingRecursiveComparison()
                .isEqualTo(exceptedSlotListPage);

        verify(questService).findAllByAccount_id(accountId);
        verify(reservationService).findActiveByQuestIdsAndDate(anyList(), any(LocalDate.class));
    }

    @Test
    void getQuestsAndSlotsByDate_doubleBlockingFailure() {
        List<ReservationWithClient> reservationsWithClient = List.of(getResWithClient(), getResWithClient());
        when(reservationService.findActiveByQuestIdsAndDate(anyList(), any(LocalDate.class))).thenReturn(reservationsWithClient);

        assertThatThrownBy(() -> reservationGetOperator.getQuestsAndSlotsByDate(date, principal))
                .isInstanceOf(RuntimeException.class)
                .hasMessageStartingWith("Double reservation");
    }

    private Quest getQuest() {
        return Quest.builder()
                .id(1)
                .questName("Quest One")
                .minPersons(1)
                .maxPersons(6)
                .autoBlock(LocalTime.MIN)
                .slotList(getSlotListJson())
                .companyId(1)
                .statuses(Status.DEFAULT_STATUSES)
                .synchronizedQuests(new HashSet<>())
                .build();
    }

    private ReservationWithClient getResWithClient() {
        return ReservationWithClient.builder()
                .id(1L)
                .timeReserve(LocalTime.parse("12:00:00"))
                .questId(1)
                .price(new BigDecimal(3000))
                .status(Status.CONFIRMED)
                .build();
    }

    private Map<String, List<Slot>> getQuestNamesAndSlots() {
        return Map.of("Quest One", getSlots());
    }

    private List<Slot> getSlots() {
        Slot slot1 = Slot.withReserve(1, getQuest(), LocalDate.now(), getResWithClient(), 3000);
        Slot slot2 = Slot.empty(1, getQuest(), LocalDate.now(), LocalTime.parse("13:00:00"), 3000);
        return List.of(slot1, slot2);
    }

    private Set<Status> getUseStatuses() {
        return Set.of(Status.CONFIRMED);
    }

    private String getSlotListJson() {
        return """
               {
                 "monday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "tuesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "wednesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "thursday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "friday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "saturday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "sunday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ]
               }""";
    }

    private SlotList getSLotList() {
        List<TimePrice> timePriceList = List.of(
                new TimePrice(LocalTime.parse("12:00"), 3000),
                new TimePrice(LocalTime.parse("13:00"), 3000)
        );

        SlotList slotList = new SlotList();
        slotList.setMonday(timePriceList);
        slotList.setTuesday(timePriceList);
        slotList.setWednesday(timePriceList);
        slotList.setThursday(timePriceList);
        slotList.setFriday(timePriceList);
        slotList.setSaturday(timePriceList);
        slotList.setSunday(timePriceList);

        return slotList;
    }
}