package ru.questsfera.questreservation.processor;

import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.model.dto.*;
import ru.questsfera.questreservation.model.entity.Quest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SlotFactory {

    public List<Slot> getSlots(Quest quest, LocalDate date, SlotList slotList,
                               Map<LocalTime, ReservationWIthClient> reservations) {

        List<Slot> slots = new ArrayList<>();
        List<TimePrice> timePriceList = switchDay(date, slotList);

        for (TimePrice timePrice : timePriceList) {
            LocalTime time = timePrice.getTime();
            Integer price = timePrice.getPrice();

            if (reservations.containsKey(time)) {
                slots.add(createSlotWithReserve(quest, date, reservations.get(time), price));
            } else {
                slots.add(createEmptySlot(quest, date, time, price));
            }
        }

        return slots;
    }

    private List<TimePrice> switchDay(LocalDate date, SlotList slotList) {
        return switch (date.getDayOfWeek()) {
            case MONDAY -> slotList.getMonday();
            case TUESDAY -> slotList.getTuesday();
            case WEDNESDAY -> slotList.getWednesday();
            case THURSDAY -> slotList.getThursday();
            case FRIDAY -> slotList.getFriday();
            case SATURDAY -> slotList.getSaturday();
            case SUNDAY -> slotList.getSunday();
        };
    }

    private Slot createSlotWithReserve(Quest quest, LocalDate date, ReservationWIthClient reserve, Integer price) {
        return Slot.fromQuestDateReservationPrice(quest, date, reserve, price);
    }

    private Slot createEmptySlot(Quest quest, LocalDate date, LocalTime time, Integer price) {
        return Slot.emptyFromQuestDateTimePrice(quest, date, time, price);
    }
}
