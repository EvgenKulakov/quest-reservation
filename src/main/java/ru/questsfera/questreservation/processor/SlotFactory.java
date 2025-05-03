package ru.questsfera.questreservation.processor;

import lombok.AllArgsConstructor;
import ru.questsfera.questreservation.dto.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class SlotFactory {
    private QuestDTO questDTO;
    private LocalDate date;
    private SlotList slotList;
    private Map<LocalTime, ReservationDTO> reservations;

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

    private Slot createSlotWithReserve(ReservationDTO reserve, Integer price) {
        return Slot.fromQuestAndReservation(questDTO, reserve, price);
    }

    private Slot createEmptySlot(LocalTime time, Integer price) {
        return Slot.emptyFromQuestDateTimePrice(questDTO, date, time, price);
    }
}
