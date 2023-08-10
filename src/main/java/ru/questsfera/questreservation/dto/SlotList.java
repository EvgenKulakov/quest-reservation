package ru.questsfera.questreservation.dto;

import java.util.LinkedHashMap;

public class SlotList {

    private LinkedHashMap<String, Integer> monday;
    private LinkedHashMap<String, Integer> tuesday;
    private LinkedHashMap<String, Integer> wednesday;
    private LinkedHashMap<String, Integer> thursday;
    private LinkedHashMap<String, Integer> friday;
    private LinkedHashMap<String, Integer> saturday;
    private LinkedHashMap<String, Integer> sunday;

    public SlotList(LinkedHashMap<String, Integer> monday, LinkedHashMap<String, Integer> tuesday,
                    LinkedHashMap<String, Integer> wednesday, LinkedHashMap<String, Integer> thursday,
                    LinkedHashMap<String, Integer> friday, LinkedHashMap<String, Integer> saturday,
                    LinkedHashMap<String, Integer> sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public SlotList() {}

    public LinkedHashMap<String, Integer> getMonday() {
        return monday;
    }

    public LinkedHashMap<String, Integer> getTuesday() {
        return tuesday;
    }

    public LinkedHashMap<String, Integer> getWednesday() {
        return wednesday;
    }

    public LinkedHashMap<String, Integer> getThursday() {
        return thursday;
    }

    public LinkedHashMap<String, Integer> getFriday() {
        return friday;
    }

    public LinkedHashMap<String, Integer> getSaturday() {
        return saturday;
    }

    public LinkedHashMap<String, Integer> getSunday() {
        return sunday;
    }
}
