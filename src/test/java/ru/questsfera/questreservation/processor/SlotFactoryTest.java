package ru.questsfera.questreservation.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.questsfera.questreservation.model.dto.*;
import ru.questsfera.questreservation.model.entity.Quest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class SlotFactoryTest {

    private SlotFactory slotFactory;
    private Quest testQuest;
    private SlotList slotList;
    private List<TimePrice> timePriceList;
    private Map<LocalTime, ReservationWIthClient> reservations;

    @BeforeEach
    void setUp() {
        slotFactory = new SlotFactory();
        testQuest = getQuest();
        slotList = new SlotList();
        timePriceList = getTimePriceList();
        reservations = new HashMap<>();
    }

    @Test
    void getSlots_withoutReservations_allEmptySlots() {
        LocalDate mondayDate = LocalDate.of(2025, 6, 2);
        slotList.setMonday(timePriceList);

        List<Slot> resultSlots = slotFactory.getSlots(testQuest, mondayDate, slotList, Collections.emptyMap());

        assertThat(resultSlots).hasSize(timePriceList.size());

        for (int i = 0; i < timePriceList.size(); i++) {
            TimePrice tp = timePriceList.get(i);
            Slot slot = resultSlots.get(i);

            assertThat(slot.getQuestId()).isEqualTo(testQuest.getId());
            assertThat(slot.getDate()).isEqualTo(mondayDate);
            assertThat(slot.getReservationId()).isNull();
            assertThat(slot.getTime()).isEqualTo(tp.getTime());
            assertThat(slot.getPrice()).isEqualTo(tp.getPrice());
        }
    }

    @Test
    void getSlots_withSomeReservations_mixedSlots() {
        LocalDate tuesdayDate = LocalDate.of(2025, 6, 3);
        slotList.setTuesday(timePriceList);

        ReservationWIthClient reservation12 = getResWithClient(tuesdayDate);
        reservations.put(LocalTime.of(12, 0), reservation12);

        List<Slot> resultSlots = slotFactory.getSlots(testQuest, tuesdayDate, slotList, reservations);

        assertThat(resultSlots).hasSize(timePriceList.size());

        for (int i = 0; i < timePriceList.size(); i++) {
            TimePrice tp = timePriceList.get(i);
            Slot slot = resultSlots.get(i);

            assertThat(slot.getQuestId()).isEqualTo(testQuest.getId());
            assertThat(slot.getDate()).isEqualTo(tuesdayDate);
            assertThat(slot.getTime()).isEqualTo(tp.getTime());
            assertThat(slot.getPrice()).isEqualTo(tp.getPrice());

            if (tp.getTime().equals(LocalTime.of(12, 0))) {
                assertThat(slot.getReservationId()).isNotNull();
                assertThat(slot.getReservationId()).isEqualTo(reservation12.getId());
            } else {
                assertThat(slot.getReservationId()).isNull();
            }
        }
    }

    @Test
    void switchDay_picksCorrectListByDayOfWeek() {
        LocalDate wednesday = LocalDate.of(2025, 6, 4);
        LocalDate thursday = LocalDate.of(2025, 6, 5);

        List<TimePrice> wednesdayList = Collections.singletonList(new TimePrice(LocalTime.of(14, 0), 300));
        List<TimePrice> thursdayList  = Collections.singletonList(new TimePrice(LocalTime.of(15, 0), 350));

        slotList.setWednesday(wednesdayList);
        slotList.setThursday(thursdayList);

        List<Slot> resultWed = slotFactory.getSlots(testQuest, wednesday, slotList, Collections.emptyMap());
        List<Slot> resultThu = slotFactory.getSlots(testQuest, thursday, slotList, Collections.emptyMap());

        assertThat(resultWed).hasSize(1);

        Slot slotWed = resultWed.getFirst();
        assertThat(slotWed.getTime()).isEqualTo(LocalTime.of(14, 0));
        assertThat(slotWed.getPrice()).isEqualTo(300);

        assertThat(resultThu).hasSize(1);
        Slot slotThu = resultThu.getFirst();
        assertThat(slotThu.getTime()).isEqualTo(LocalTime.of(15, 0));
        assertThat(slotThu.getPrice()).isEqualTo(350);
    }

    private Quest getQuest() {
        return Quest.builder()
                .id(1)
                .questName("Quest One")
                .minPersons(1)
                .maxPersons(6)
                .autoBlock(LocalTime.MIN)
                .companyId(1)
                .statuses(Status.DEFAULT_STATUSES)
                .synchronizedQuests(new HashSet<>())
                .build();
    }

    private ReservationWIthClient getResWithClient(LocalDate dateReserve) {
        return ReservationWIthClient.builder()
                .id(1L)
                .dateReserve(dateReserve)
                .timeReserve(LocalTime.parse("12:00:00"))
                .questId(1)
                .price(new BigDecimal(3000))
                .status(Status.CONFIRMED)
                .build();
    }

    private List<TimePrice> getTimePriceList() {
        return Arrays.asList(
                new TimePrice(LocalTime.of(9, 0),  80),
                new TimePrice(LocalTime.of(10, 0), 120),
                new TimePrice(LocalTime.of(12, 0), 3000)
        );
    }
}
