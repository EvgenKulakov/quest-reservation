package ru.questsfera.questreservation.processor;

import lombok.AllArgsConstructor;
import ru.questsfera.questreservation.model.dto.*;
import ru.questsfera.questreservation.model.entity.Quest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class SlotFactory {
    private Quest quest;
    private LocalDate date;
    private SlotList slotList;
    private Map<LocalTime, ReservationWIthClient> reservations;

    public List<Slot> getActualSlots() {
        List<Slot> slots = new ArrayList<>();
        List<TimePrice> timePriceList = switchDay(date);

        for (TimePrice timePrice : timePriceList) {
            LocalTime time = timePrice.getTime();
            Integer price = timePrice.getPrice();

            if (reservations.containsKey(time)) {
                slots.add(createSlotWithReserve(reservations.get(time), price));
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

    private Slot createSlotWithReserve(ReservationWIthClient reserve, Integer price) {
        return Slot.fromQuestDateReservationPrice(quest, date, reserve, price);
    }

    private Slot createEmptySlot(LocalTime time, Integer price) {
        return Slot.emptyFromQuestDateTimePrice(quest, date, time, price);
    }
}
