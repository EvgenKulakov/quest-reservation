package ru.questsfera.questreservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"autoBlock"})
public class Slot {
    private Quest quest;
    private StatusType statusType;
    private Reservation reservation;
    @JsonFormat(pattern = "dd-MM-yyyy (EEEE)", locale = "ru")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private Integer price;
    private LocalTime autoBlock;

    @JsonIgnore
    public String getJSON() {
        return SlotMapper.createJSONSlot(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(quest, slot.quest)
                && Objects.equals(date, slot.date)
                && Objects.equals(time, slot.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quest, date, time);
    }
}
