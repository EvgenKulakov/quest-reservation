package ru.questsfera.questreservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class TimePrice implements Comparable<TimePrice> {

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private Integer price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePrice timePrice = (TimePrice) o;
        return Objects.equals(time, timePrice.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }

    @Override
    public int compareTo(TimePrice other) {
        return this.time.compareTo(other.time);
    }
}
