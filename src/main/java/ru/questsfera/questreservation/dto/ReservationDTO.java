package ru.questsfera.questreservation.dto;

import lombok.*;
import ru.questsfera.questreservation.entity.Client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReservationDTO {

    private Long id;
    private LocalDate dateReserve;
    private LocalTime timeReserve;
    private LocalDateTime dateAndTimeCreated;
    private LocalDateTime timeLastChange;
    private LocalTime changedSlotTime;
    private Integer questId;
    private StatusType statusType;
    private String sourceReserve;
    private BigDecimal price;
    private BigDecimal changedPrice;
    private Client client;
    private Integer countPersons;
    private String adminComment;
    private String clientComment;
    private String historyMessages;

    public ReservationDTO editUsingResForm(ResFormDTO resFormDTO) {
        this.setStatusType(resFormDTO.getStatusType());
        this.setCountPersons(resFormDTO.getCountPersons());
        this.setAdminComment(resFormDTO.getAdminComment());
        this.setClientComment(resFormDTO.getClientComment());

        this.getClient().setFirstName(resFormDTO.getFirstName());
        this.getClient().setLastName(resFormDTO.getLastName());
        this.getClient().setPhones(resFormDTO.getPhone());
        this.getClient().setEmails(resFormDTO.getEmail());
        return this;
    }

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
