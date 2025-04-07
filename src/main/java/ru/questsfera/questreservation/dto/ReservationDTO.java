package ru.questsfera.questreservation.dto;

import lombok.Value;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Quest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Value
public class ReservationDTO {

    Long id;
    LocalDate dateReserve;
    LocalTime timeReserve;
    LocalDateTime dateAndTimeCreated;
    LocalDateTime timeLastChange;
    LocalTime changedSlotTime;
    QuestDTO questDTO;
    StatusType statusType;
    String sourceReserve;
    BigDecimal price;
    BigDecimal changedPrice;
    Client client;
    Integer countPersons;
    String adminComment;
    String clientComment;
    String historyMessages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationDTO that)) return false;
        return id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
