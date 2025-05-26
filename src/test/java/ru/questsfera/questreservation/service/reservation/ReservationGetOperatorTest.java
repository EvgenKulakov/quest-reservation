package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.mapper.QuestMapper;
import ru.questsfera.questreservation.model.dto.*;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.model.entity.Status;
import ru.questsfera.questreservation.service.quest.QuestService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationGetOperatorTest {

    @Mock ReservationService reservationService;
    @Mock QuestService questService;
    @Mock Principal principal;
    @Mock QuestMapper questMapper;
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
        List<ReservationDTO> reservationDTOs = List.of(getResDto());
        when(reservationService.findActiveByQuestIdsAndDate(anyList(), any(LocalDate.class))).thenReturn(reservationDTOs);
        when(questMapper.toDto(getQuest())).thenReturn(getQuestDto());

        SlotListPageDTO actualSlotListPageDTO = reservationGetOperator.getQuestsAndSlotsByDate(date, principal);
        SlotListPageDTO exceptedSlotListPageDTO = new SlotListPageDTO(getQuestNamesAndSlots(), getUseStatuses());

        assertThat(actualSlotListPageDTO)
                .usingRecursiveComparison()
                .isEqualTo(exceptedSlotListPageDTO);

        verify(questService).findAllByAccount_login(accountLogin);
        verify(reservationService).findActiveByQuestIdsAndDate(anyList(), any(LocalDate.class));
    }

    @Test
    void getQuestsAndSlotsByDate_doubleBlockingFailure() {
        List<ReservationDTO> reservationDTOs = List.of(getResDto(), getResDto());
        when(reservationService.findActiveByQuestIdsAndDate(anyList(), any(LocalDate.class))).thenReturn(reservationDTOs);

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
                .statuses("NEW_RESERVE,CANCEL,CONFIRMED,NOT_COME,COMPLETED")
                .synchronizedQuests(new HashSet<>())
                .build();
    }

    private ReservationDTO getResDto() {
        return ReservationDTO.builder()
                .id(1L)
                .timeReserve(LocalTime.parse("12:00:00"))
                .questId(1)
                .price(new BigDecimal(3000))
                .statusType(StatusType.CONFIRMED)
                .build();
    }

    private Map<String, List<Slot>> getQuestNamesAndSlots() {
        Slot slot1 = Slot.fromQuestDateReservationPrice(getQuestDto(), LocalDate.now(), getResDto(), 3000);
        Slot slot2 = Slot.emptyFromQuestDateTimePrice(getQuestDto(), LocalDate.now(), LocalTime.parse("13:00:00"), 3000);
        return Map.of("Quest One", List.of(slot1, slot2));
    }

    private QuestDTO getQuestDto() {
        return QuestDTO.builder()
                .id(1)
                .questName("Quest One")
                .minPersons(1)
                .maxPersons(6)
                .autoBlock(LocalTime.MIN)
                .companyId(1)
                .statuses(Status.getUserStatuses())
                .synchronizedQuests(new HashSet<>())
                .build();
    }

    private Set<StatusType> getUseStatuses() {
        return Set.of(StatusType.CONFIRMED);
    }
}