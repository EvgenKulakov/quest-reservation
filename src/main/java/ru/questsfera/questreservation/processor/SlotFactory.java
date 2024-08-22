package ru.questsfera.questreservation.processor;

import lombok.AllArgsConstructor;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.dto.TimePrice;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@AllArgsConstructor
public class SlotFactory {
    private Quest quest;
    private LocalDate date;
    private SlotList slotList;
    private Map<LocalTime, Reservation> reservations;

    public List<Slot> getActualSlots() {

        List<Slot> slots = new ArrayList<>();

        List<TimePrice> timePriceList = switchDay(date);

        for (TimePrice timePrice : timePriceList) {
            LocalTime time = timePrice.getTime();
            Integer price = timePrice.getPrice();

            if (reservations.containsKey(time)) {
                slots.add(createSlotWithReserve(time, price, reservations.get(time)));
            } else {
                slots.add(createEmptySlot(time, price));
            }
        }

        return slots;
    }

    private List<TimePrice> switchDay(LocalDate date) {
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

    private Slot createSlotWithReserve(LocalTime time, Integer price, Reservation reserve) {
        return new Slot(quest, reserve, date, time, price);
    }

    private Slot createEmptySlot(LocalTime time, Integer price) {
        return new Slot(quest, date, time, price);
    }
}
