package ru.questsfera.questreservation.processor;

import ru.questsfera.questreservation.dto.SlotList;

public class SlotListFactory {

    public static void makeSlotListEveryDay(SlotList slotList) {
        slotList.setTuesday(slotList.getMonday());
        slotList.setWednesday(slotList.getMonday());
        slotList.setThursday(slotList.getMonday());
        slotList.setFriday(slotList.getMonday());
        slotList.setSaturday(slotList.getMonday());
        slotList.setSunday(slotList.getMonday());
    }
}
