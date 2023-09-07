package ru.questsfera.questreservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;
import java.util.Objects;

public class TimePrice implements Comparable<TimePrice> {

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private Integer price;

    public TimePrice() {}

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

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
