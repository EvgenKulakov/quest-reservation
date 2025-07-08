package ru.questsfera.questreservation.model.dto;

import lombok.*;
import ru.questsfera.questreservation.model.entity.Client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReservationWithClient {

    private Long id;
    private LocalDate dateReserve;
    private LocalTime timeReserve;
    private LocalDateTime dateAndTimeCreated;
    private LocalDateTime timeLastChange;
    private LocalTime changedSlotTime;
    private Integer questId;
    private Status status;
    private String sourceReserve;
    private BigDecimal price;
    private BigDecimal changedPrice;
    private Client client;
    private Integer countPersons;
    private String adminComment;
    private String clientComment;
    private String historyMessages;

    public ReservationWithClient editWithResForm(ReservationForm reservationForm) {
        this.setStatus(reservationForm.getStatus());
        this.setCountPersons(reservationForm.getCountPersons());
        this.setAdminComment(reservationForm.getAdminComment());
        this.setClientComment(reservationForm.getClientComment());

        this.getClient().setFirstName(reservationForm.getFirstName());
        this.getClient().setLastName(reservationForm.getLastName());
        this.getClient().setPhones(reservationForm.getPhone());
        this.getClient().setEmails(reservationForm.getEmail());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationWithClient that)) return false;
        return id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
