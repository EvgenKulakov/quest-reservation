package ru.questsfera.questreservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SlotList {

    private LinkedHashMap<LocalTime, Integer> monday;
    private LinkedHashMap<LocalTime, Integer> tuesday;
    private LinkedHashMap<LocalTime, Integer> wednesday;
    private LinkedHashMap<LocalTime, Integer> thursday;
    private LinkedHashMap<LocalTime, Integer> friday;
    private LinkedHashMap<LocalTime, Integer> saturday;
    private LinkedHashMap<LocalTime, Integer> sunday;

    public SlotList(LinkedHashMap<LocalTime, Integer> monday, LinkedHashMap<LocalTime, Integer> tuesday,
                    LinkedHashMap<LocalTime, Integer> wednesday, LinkedHashMap<LocalTime, Integer> thursday,
                    LinkedHashMap<LocalTime, Integer> friday, LinkedHashMap<LocalTime, Integer> saturday,
                    LinkedHashMap<LocalTime, Integer> sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public SlotList(LinkedHashMap<LocalTime, Integer> weekday,
                    LinkedHashMap<LocalTime, Integer> weekend) {
        this.monday = weekday;
        this.tuesday = weekday;
        this.wednesday = weekday;
        this.thursday = weekday;
        this.friday = weekday;
        this.saturday = weekend;
        this.sunday = weekend;

    }

    public SlotList(LinkedHashMap<LocalTime, Integer> everyday) {
        this.monday = everyday;
        this.tuesday = everyday;
        this.wednesday = everyday;
        this.thursday = everyday;
        this.friday = everyday;
        this.saturday = everyday;
        this.sunday = everyday;
    }

    public SlotList() {}

    @JsonIgnore
    public List<LinkedHashMap<LocalTime, Integer>> getAllDays() {
        List<LinkedHashMap<LocalTime, Integer>> allDays = new ArrayList<>();
        allDays.add(monday);
        allDays.add(tuesday);
        allDays.add(wednesday);
        allDays.add(thursday);
        allDays.add(friday);
        allDays.add(saturday);
        allDays.add(sunday);

        return allDays;
    }


    public LinkedHashMap<LocalTime, Integer> getMonday() {
        return monday;
    }

    public void setMonday(LinkedHashMap<LocalTime, Integer> monday) {
        this.monday = monday;
    }

    public LinkedHashMap<LocalTime, Integer> getTuesday() {
        return tuesday;
    }

    public void setTuesday(LinkedHashMap<LocalTime, Integer> tuesday) {
        this.tuesday = tuesday;
    }

    public LinkedHashMap<LocalTime, Integer> getWednesday() {
        return wednesday;
    }

    public void setWednesday(LinkedHashMap<LocalTime, Integer> wednesday) {
        this.wednesday = wednesday;
    }

    public LinkedHashMap<LocalTime, Integer> getThursday() {
        return thursday;
    }

    public void setThursday(LinkedHashMap<LocalTime, Integer> thursday) {
        this.thursday = thursday;
    }

    public LinkedHashMap<LocalTime, Integer> getFriday() {
        return friday;
    }

    public void setFriday(LinkedHashMap<LocalTime, Integer> friday) {
        this.friday = friday;
    }

    public LinkedHashMap<LocalTime, Integer> getSaturday() {
        return saturday;
    }

    public void setSaturday(LinkedHashMap<LocalTime, Integer> saturday) {
        this.saturday = saturday;
    }

    public LinkedHashMap<LocalTime, Integer> getSunday() {
        return sunday;
    }

    public void setSunday(LinkedHashMap<LocalTime, Integer> sunday) {
        this.sunday = sunday;
    }
}
