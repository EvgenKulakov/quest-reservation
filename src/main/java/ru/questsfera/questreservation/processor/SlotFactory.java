package ru.questsfera.questreservation.processor;

import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.entity.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class SlotFactory {
    private Quest quest;
    private LocalDate date;
    private SlotList slotList;
    private LinkedList<Reservation> reservations;

    public SlotFactory(Quest quest, LocalDate date, SlotList slotList,
                       LinkedList<Reservation> reservations) {
        this.quest = quest;
        this.date = date;
        this.slotList = slotList;
        this.reservations = reservations;
    }

    public List<Slot> getSlots() {
        checkReservations();

        List<Slot> slots = new ArrayList<>();

        HashMap<String, Integer> slotListMap = switchDay(date);

        for (Map.Entry<String, Integer> pair : slotListMap.entrySet()) {
            LocalTime time = LocalTime.parse(pair.getKey());
            Integer price = pair.getValue();

            if (!reservations.isEmpty() && reservations.peek().getTimeReserve().equals(time)) {
                slots.add(createSlotWithReserve(time, price, reservations.pop()));
            } else {
                slots.add(createEmptySlot(time, price));
            }
        }

        return slots;
    }

    private LinkedHashMap<String, Integer> switchDay(LocalDate date) {
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
            if (reservations.get(i).getTimeReserve().equals(reservations.get(i-1).getTimeReserve())) {
                throw new RuntimeException("Два бронирования на одно и тоже время");
            }
        }
    }

    private Slot createSlotWithReserve(LocalTime time, Integer price, Reservation reserve) {
        Status status = reserve.getStatus();
        LocalTime autoBlock = reserve.getAutoBlock();
        Slot slot = new Slot(quest, status, reserve, date, time, price, autoBlock);
        return slot;
    }

    private Slot createEmptySlot(LocalTime time, Integer price) {
        Slot slot = new Slot(quest, new Status(StatusType.EMPTY),
                null, date, time, price, null);
        return slot;
    }
}
