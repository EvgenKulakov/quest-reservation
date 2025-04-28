package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.dto.*;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.service.quest.QuestService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    List<Integer> questIds;
    List<ReservationDTO> reservationDTOs;

    @BeforeEach
    void setUp() {
        date = LocalDate.now();
        accountLogin = "login";
        quests = Set.of(getQuest1(), getQuest2());
        questIds = quests.stream().map(Quest::getId).toList();

        when(principal.getName()).thenReturn(accountLogin);
        when(questService.findAllByAccount_login(accountLogin)).thenReturn(quests);
    }

    @Test
    void getQuestsAndSlotsByDate_success() {
        reservationDTOs = List.of(getResDto1(), getResDto2());
        when(reservationService.findActiveByQuestIdsAndDate(questIds, date)).thenReturn(reservationDTOs);

        SlotListPageDTO actualSlotListPageDTO = reservationGetOperator.getQuestsAndSlotsByDate(date, principal);
        SlotListPageDTO exceptedSlotListPageDTO = new SlotListPageDTO(getQuestNameAndSlots(), getUseStatuses());

        assertThat(actualSlotListPageDTO)
                .usingRecursiveComparison()
                .isEqualTo(exceptedSlotListPageDTO);

        verify(questService).findAllByAccount_login(accountLogin);
        verify(reservationService).findActiveByQuestIdsAndDate(questIds, date);
    }

    @Test
    void getQuestsAndSlotsByDate_doubleBlockingFailure() {
        reservationDTOs = List.of(getResDto1(), getResDto1());
        when(reservationService.findActiveByQuestIdsAndDate(questIds, date)).thenReturn(reservationDTOs);

        assertThatThrownBy(() -> reservationGetOperator.getQuestsAndSlotsByDate(date, principal))
                .isInstanceOf(RuntimeException.class)
                .hasMessageStartingWith("Double reservation");
    }

    private Quest getQuest1() {
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
                .statuses("NEW_RESERVE,CANCEL,CONFIRMED,NOT_COME,COMPLETED")
                .synchronizedQuests(new HashSet<>())
                .build();
    }

    private Quest getQuest2() {
        String slotListQuestTwo = """
                {
                  "monday" : [ {"time" : "12:30","price" : 1500}, {"time" : "14:00", "price" : 1500} ],
                  "tuesday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500} ],
                  "wednesday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500} ],
                  "thursday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500} ],
                  "friday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500} ],
                  "saturday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500} ],
                  "sunday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500} ]
                }""";

        return Quest.builder()
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
    }

    private ReservationDTO getResDto1() {
        return ReservationDTO.builder()
                .id(1L)
                .timeReserve(LocalTime.parse("12:00:00"))
                .questId(1)
                .statusType(StatusType.CONFIRMED)
                .build();
    }

    private ReservationDTO getResDto2() {
        return ReservationDTO.builder()
                .id(2L)
                .timeReserve(LocalTime.parse("14:00:00"))
                .questId(2)
                .statusType(StatusType.BLOCK)
                .build();
    }

    private Map<String, List<Slot>> getQuestNameAndSlots() {
        Map<String, List<Slot>> questNameAndSlots = new LinkedHashMap<>();

        Quest quest1 = getQuest1();
        Slot slot1 = new Slot(new QuestDTO(quest1), getResDto1(), LocalDate.now(), LocalTime.parse("12:00:00"), 3000);
        Slot slot2 = new Slot(new QuestDTO(quest1), LocalDate.now(), LocalTime.parse("13:00:00"), 3000);
        questNameAndSlots.put("Quest One", List.of(slot1, slot2));

        Quest quest2 = getQuest2();
        Slot slot3 = new Slot(new QuestDTO(quest2), LocalDate.now(), LocalTime.parse("12:30:00"), 1500);
        Slot slot4 = new Slot(new QuestDTO(quest2), getResDto2(), LocalDate.now(), LocalTime.parse("14:00:00"), 1500);
        questNameAndSlots.put("Quest Two", List.of(slot3, slot4));

        return questNameAndSlots;
    }

    private Set<StatusType> getUseStatuses() {
        return Set.of(StatusType.CONFIRMED, StatusType.BLOCK);
    }
}