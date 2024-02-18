package ru.questsfera.questreservation.processor;

import lombok.AllArgsConstructor;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.dto.StatusType;
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
    private LinkedList<Reservation> reservations;

    public List<Slot> getActualSlots() {
        checkReservations();

        List<Slot> slots = new ArrayList<>();

        List<TimePrice> timePriceList = switchDay(date);

        for (TimePrice timePrice : timePriceList) {
            LocalTime time = timePrice.getTime();
            Integer price = timePrice.getPrice();

            while (!reservations.isEmpty()
                    && reservations.peek().getStatusType().equals(StatusType.CANCEL)) {
                reservations.pop();
            }

            if (!reservations.isEmpty() && reservations.peek().getTimeReserve().equals(time)) {
                slots.add(createSlotWithReserve(time, price, reservations.pop()));
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

    private void checkReservations() {
        for (int i = 1; i < reservations.size(); i++) {
            if (reservations.get(i).getTimeReserve().equals(reservations.get(i-1).getTimeReserve())
                    && !reservations.get(i).getStatusType().equals(StatusType.CANCEL)
                    && !reservations.get(i-1).getStatusType().equals(StatusType.CANCEL)) {
                throw new RuntimeException("Два бронирования на одно и тоже время");
            }
        }
    }

    private Slot createSlotWithReserve(LocalTime time, Integer price, Reservation reserve) {
        StatusType status = reserve.getStatusType();
        LocalTime autoBlock = quest.getAutoBlock();
        Slot slot = new Slot(quest, status, reserve, date, time, price, autoBlock);
        return slot;
    }

    private Slot createEmptySlot(LocalTime time, Integer price) {
        Slot slot = new Slot(quest, StatusType.EMPTY,
                null, date, time, price, null);
        return slot;
    }
}
