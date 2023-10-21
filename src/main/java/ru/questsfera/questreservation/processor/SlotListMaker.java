package ru.questsfera.questreservation.processor;

import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.dto.TimePrice;

import java.util.List;
import java.util.stream.IntStream;

public class SlotListMaker {

    public static void makeEqualDays(SlotList slotList) {
        slotList.setTuesday(slotList.getMonday());
        slotList.setWednesday(slotList.getMonday());
        slotList.setThursday(slotList.getMonday());
        slotList.setFriday(slotList.getMonday());
        slotList.setSaturday(slotList.getMonday());
        slotList.setSunday(slotList.getMonday());
    }

    public static void addDefaultValues(SlotList slotList) {
        List<TimePrice> defaultDay = IntStream
                .range(0, 10)
                .mapToObj(i -> new TimePrice())
                .toList();
        slotList.setMonday(defaultDay);
        makeEqualDays(slotList);
    }
}
