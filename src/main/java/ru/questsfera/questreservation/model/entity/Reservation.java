package ru.questsfera.questreservation.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.questsfera.questreservation.model.dto.ReservationForm;
import ru.questsfera.questreservation.model.dto.Slot;
import ru.questsfera.questreservation.model.dto.Status;
import ru.questsfera.questreservation.model.dto.Status;

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
    @JoinColumn(name = "status")
    private Status status;

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

    public static Reservation fromResFormAndSlot(ReservationForm reservationForm, Slot slot) {
        Reservation reservation = new Reservation();
        reservation.setDateReserve(slot.getDate());
        reservation.setTimeReserve(slot.getTime());
        reservation.setDateAndTimeCreated(LocalDateTime.now());
        reservation.setQuestId(slot.getQuestId());
        reservation.setStatus(reservationForm.getStatus());
        reservation.setPrice(new BigDecimal(slot.getPrice()));
        reservation.setCountPersons(reservationForm.getCountPersons());
        reservation.setAdminComment(reservationForm.getAdminComment());
        reservation.setClientComment(reservationForm.getClientComment());
        return reservation;
    }

    public static Reservation blockReservationFromSlot(Slot slot) {
        Reservation reservation = new Reservation();
        reservation.setDateReserve(slot.getDate());
        reservation.setTimeReserve(slot.getTime());
        reservation.setDateAndTimeCreated(LocalDateTime.now());
        reservation.setQuestId(slot.getQuestId());
        reservation.setStatus(Status.BLOCK);
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
