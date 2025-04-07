package ru.questsfera.questreservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import lombok.*;
import ru.questsfera.questreservation.dto.StatusType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations", schema = "quest_reservations_db")
@JsonIgnoreProperties({"questId"})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    //TODO: statuses
    @Enumerated(value = EnumType.STRING)
    @JoinColumn(name = "status_type")
    private StatusType statusType;

    @Column(name = "source_reserve")
    private String sourceReserve;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "changed_price")
    private BigDecimal changedPrice;

//    @ManyToOne
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
