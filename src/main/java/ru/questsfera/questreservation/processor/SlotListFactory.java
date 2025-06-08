package ru.questsfera.questreservation.processor;

import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.model.dto.SlotList;
import ru.questsfera.questreservation.model.dto.SlotListTypeBuild;
import ru.questsfera.questreservation.model.dto.TimePrice;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class SlotListFactory {

    public SlotList makeByType(SlotList slotList, SlotListTypeBuild typeBuild) {
        return switch (typeBuild) {
            case EQUAL_DAYS -> makeEqualDays(slotList);
            case WEEKDAYS_WEEKENDS -> makeWeekdaysWeekends(slotList);
            case DIFFERENT_DAYS -> slotList;
        };
    }

    private SlotList makeEqualDays(SlotList slotListSrc) {
        SlotList slotListResult = new SlotList();

        slotListResult.setMonday(slotListSrc.getMonday());
        slotListResult.setTuesday(slotListSrc.getMonday());
        slotListResult.setWednesday(slotListSrc.getMonday());
        slotListResult.setThursday(slotListSrc.getMonday());
        slotListResult.setFriday(slotListSrc.getMonday());
        slotListResult.setSaturday(slotListSrc.getMonday());
        slotListResult.setSunday(slotListSrc.getMonday());

        return slotListResult;
    }

    private SlotList makeWeekdaysWeekends(SlotList slotListSrc) {
        SlotList slotListResult = new SlotList();

        slotListResult.setMonday(slotListSrc.getMonday());
        slotListResult.setTuesday(slotListSrc.getMonday());
        slotListResult.setWednesday(slotListSrc.getMonday());
        slotListResult.setThursday(slotListSrc.getMonday());
        slotListResult.setFriday(slotListSrc.getMonday());
        slotListResult.setSaturday(slotListSrc.getSaturday());
        slotListResult.setSunday(slotListSrc.getSaturday());

        return slotListResult;
    }

    public SlotList createDefaultValues() {
        List<TimePrice> defaultDay = IntStream
                .range(0, 10)
                .mapToObj(i -> new TimePrice())
                .toList();

        SlotList slotList = new SlotList();
        slotList.setMonday(defaultDay);

        return makeEqualDays(slotList);
    }
}
