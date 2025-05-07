package ru.questsfera.questreservation.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SlotList {

    private List<TimePrice> monday;
    private List<TimePrice> tuesday;
    private List<TimePrice> wednesday;
    private List<TimePrice> thursday;
    private List<TimePrice> friday;
    private List<TimePrice> saturday;
    private List<TimePrice> sunday;

    @JsonIgnore
    public List<List<TimePrice>> getAllDays() {
        List<List<TimePrice>> allDays = new ArrayList<>();
        Collections.addAll(allDays, monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        return allDays;
    }
}
