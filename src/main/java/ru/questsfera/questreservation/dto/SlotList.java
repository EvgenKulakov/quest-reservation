package ru.questsfera.questreservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlotList {

    private List<TimePrice> monday;
    private List<TimePrice> tuesday;
    private List<TimePrice> wednesday;
    private List<TimePrice> thursday;
    private List<TimePrice> friday;
    private List<TimePrice> saturday;
    private List<TimePrice> sunday;

    public SlotList() {}

    @JsonIgnore
    public List<List<TimePrice>> getAllDays() {
        List<List<TimePrice>> allDays = new ArrayList<>();
        Collections.addAll(allDays, monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        return allDays;
    }

    public List<TimePrice> getMonday() {
        return monday;
    }

    public void setMonday(List<TimePrice> monday) {
        this.monday = monday;
    }

    public List<TimePrice> getTuesday() {
        return tuesday;
    }

    public void setTuesday(List<TimePrice> tuesday) {
        this.tuesday = tuesday;
    }

    public List<TimePrice> getWednesday() {
        return wednesday;
    }

    public void setWednesday(List<TimePrice> wednesday) {
        this.wednesday = wednesday;
    }

    public List<TimePrice> getThursday() {
        return thursday;
    }

    public void setThursday(List<TimePrice> thursday) {
        this.thursday = thursday;
    }

    public List<TimePrice> getFriday() {
        return friday;
    }

    public void setFriday(List<TimePrice> friday) {
        this.friday = friday;
    }

    public List<TimePrice> getSaturday() {
        return saturday;
    }

    public void setSaturday(List<TimePrice> saturday) {
        this.saturday = saturday;
    }

    public List<TimePrice> getSunday() {
        return sunday;
    }

    public void setSunday(List<TimePrice> sunday) {
        this.sunday = sunday;
    }
}
