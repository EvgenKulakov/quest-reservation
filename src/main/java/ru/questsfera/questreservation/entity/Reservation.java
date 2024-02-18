package ru.questsfera.questreservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.dto.StatusType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reservations", schema = "quest_reservations_db")
@JsonIgnoreProperties({"quest", "admin"})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @ManyToOne
    @JoinColumn(name = "quest_id")
    private Quest quest;

    @Enumerated(value = EnumType.STRING)
    @JoinColumn(name = "status_type")
    private StatusType statusType;

    @Column(name = "source_reserve")
    private String sourceReserve;

    @Column(name = "changed_price")
    private Integer changedPrice;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "count_persons")
    private int countPersons;

    @Column(name = "admin_comment")
    private String adminComment;

    @Column(name = "client_comment")
    private String clientComment;

    @Column(name = "history_messages")
    private String historyMessages;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public void addClient(Client client) {
        client.getReservations().add(this);
        this.client = client;
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
