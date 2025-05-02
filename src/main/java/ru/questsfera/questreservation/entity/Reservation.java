package ru.questsfera.questreservation.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.questsfera.questreservation.dto.ResFormDTO;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.StatusType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservations_seq")
    @SequenceGenerator(name = "reservations_seq", sequenceName = "reservations_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "date_reserve")
    private LocalDate dateReserve;

    @Column(name = "time_reserve")
    private LocalTime timeReserve;

    @Column(name = "time_created")
    private LocalDateTime dateAndTimeCreated;

    @Column(name = "time_last_change")
    private LocalDateTime timeLastChange;

    @Column(name = "changed_slot_time")
    private LocalTime changedSlotTime;

    @Column(name = "quest_id")
    private Integer questId;

    @Enumerated(value = EnumType.STRING)
    @JoinColumn(name = "status_type")
    private StatusType statusType;

    @Column(name = "source_reserve")
    private String sourceReserve;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "changed_price")
    private BigDecimal changedPrice;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "count_persons")
    private Integer countPersons;

    @Column(name = "admin_comment")
    private String adminComment;

    @Column(name = "client_comment")
    private String clientComment;

    // TODO: new table historyMessages
    @Column(name = "history_messages")
    private String historyMessages;

    public static Reservation fromResFormAndSlot(ResFormDTO resFormDTO, Slot slot) {
        Reservation reservation = new Reservation();
        reservation.setDateReserve(slot.getDate());
        reservation.setTimeReserve(slot.getTime());
        reservation.setDateAndTimeCreated(LocalDateTime.now());
        reservation.setQuestId(slot.getQuestId());
        reservation.setStatusType(resFormDTO.getStatusType());
        reservation.setPrice(new BigDecimal(slot.getPrice()));
        reservation.setCountPersons(resFormDTO.getCountPersons());
        reservation.setAdminComment(resFormDTO.getAdminComment());
        reservation.setClientComment(resFormDTO.getClientComment());
        return reservation;
    }

    public static Reservation blockReservationFromSlot(Slot slot) {
        Reservation reservation = new Reservation();
        reservation.setDateReserve(slot.getDate());
        reservation.setTimeReserve(slot.getTime());
        reservation.setDateAndTimeCreated(LocalDateTime.now());
        reservation.setQuestId(slot.getQuestId());
        reservation.setStatusType(StatusType.BLOCK);
        return reservation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation that)) return false;
        return id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
