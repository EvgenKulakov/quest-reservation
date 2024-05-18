package ru.questsfera.questreservation.cache.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationCache implements Cache {

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
    private ClientCache clientCache;
    private Integer countPersons;
    private String adminComment;
    private String clientComment;
    private String historyMessages;

    public ReservationCache(Reservation reservation) {
        this.id = reservation.getId();
        this.dateReserve = reservation.getDateReserve();
        this.timeReserve = reservation.getTimeReserve();
        this.dateAndTimeCreated = reservation.getDateAndTimeCreated();
        this.timeLastChange = reservation.getTimeLastChange();
        this.changedSlotTime = reservation.getChangedSlotTime();
        this.questId = reservation.getQuest().getId();
        this.statusType = reservation.getStatusType();
        this.sourceReserve = reservation.getSourceReserve();
        this.price = reservation.getPrice();
        this.changedPrice = reservation.getChangedPrice();
        this.clientCache = new ClientCache(reservation.getClient());
        this.countPersons = reservation.getCountPersons();
        this.adminComment = reservation.getAdminComment();
        this.clientComment = reservation.getClientComment();
        this.historyMessages = reservation.getHistoryMessages();
    }

    @Override
    @JsonIgnore
    public String getCacheId() {
        return String.format("reserve:[quest:%d][datetime:%s-%s]",
                this.getQuestId(),
                this.getDateReserve().format(DateTimeFormatter.ofPattern("dd-MM")),
                this.getTimeReserve().format(DateTimeFormatter.ofPattern("HH-mm")));
    }
}
