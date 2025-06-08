package ru.questsfera.questreservation.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.questsfera.questreservation.model.dto.SlotList;
import ru.questsfera.questreservation.model.dto.SlotListTypeBuild;
import ru.questsfera.questreservation.model.dto.TimePrice;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SlotListFactoryTest {

    private SlotListFactory slotListFactory;

    @BeforeEach
    void setUp() {
        slotListFactory = new SlotListFactory();
    }

    @Test
    void makeByType_shouldCreateEqualDaysSlotList() {
        SlotList slotListSrc = new SlotList();
        List<TimePrice> monday = getTimePriceList1();
        slotListSrc.setMonday(monday);

        SlotList slotListResult = slotListFactory.makeByType(slotListSrc, SlotListTypeBuild.EQUAL_DAYS);

        assertThat(slotListResult.getMonday()).isEqualTo(monday);
        assertThat(slotListResult.getTuesday()).isEqualTo(monday);
        assertThat(slotListResult.getWednesday()).isEqualTo(monday);
        assertThat(slotListResult.getThursday()).isEqualTo(monday);
        assertThat(slotListResult.getFriday()).isEqualTo(monday);
        assertThat(slotListResult.getSaturday()).isEqualTo(monday);
        assertThat(slotListResult.getSunday()).isEqualTo(monday);
    }

    @Test
    void makeByType_shouldCreateWeekdaysWeekendsSlotList() {
        SlotList slotListSrc = new SlotList();
        List<TimePrice> weekday = getTimePriceList1();
        List<TimePrice> weekend = getTimePriceList2();

        slotListSrc.setMonday(weekday);
        slotListSrc.setSaturday(weekend);

        SlotList result = slotListFactory.makeByType(slotListSrc, SlotListTypeBuild.WEEKDAYS_WEEKENDS);

        assertThat(result.getMonday()).isEqualTo(weekday);
        assertThat(result.getFriday()).isEqualTo(weekday);
        assertThat(result.getSaturday()).isEqualTo(weekend);
        assertThat(result.getSunday()).isEqualTo(weekend);
    }

    @Test
    void makeByType_shouldReturnSameListForDifferentDays() {
        SlotList slotListSrc = new SlotList();
        List<TimePrice> monday = getTimePriceList1();
        slotListSrc.setMonday(monday);

        SlotList result = slotListFactory.makeByType(slotListSrc, SlotListTypeBuild.DIFFERENT_DAYS);

        assertThat(result).isSameAs(slotListSrc);
    }

    @Test
    void createDefaultValues_shouldReturnSlotListWithDefaultValues() {
        SlotList result = slotListFactory.createDefaultValues();

        assertThat(result.getMonday()).hasSize(10);
        assertThat(result.getTuesday()).isEqualTo(result.getMonday());
        assertThat(result.getSunday()).isEqualTo(result.getMonday());
    }

    private List<TimePrice> getTimePriceList1() {
        return Arrays.asList(
                new TimePrice(LocalTime.of(9, 0),  80),
                new TimePrice(LocalTime.of(10, 0), 120),
                new TimePrice(LocalTime.of(12, 0), 3000)
        );
    }

    private List<TimePrice> getTimePriceList2() {
        return Arrays.asList(
                new TimePrice(LocalTime.of(13, 0),  800),
                new TimePrice(LocalTime.of(14, 0), 1200),
                new TimePrice(LocalTime.of(15, 0), 3000)
        );
    }
}