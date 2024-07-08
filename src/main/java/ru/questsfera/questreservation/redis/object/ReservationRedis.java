package ru.questsfera.questreservation.redis.object;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.RedisCalendar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("reserve")
public class ReservationRedis {

    @Id private Long id;
    @TimeToLive private Long timeToLive;
    @Indexed private LocalDate dateReserve;
    @Indexed private LocalTime timeReserve;
    @Indexed private Integer questId;

    private LocalDateTime dateAndTimeCreated;
    private LocalDateTime timeLastChange;
    private LocalTime changedSlotTime;
    private StatusType statusType;
    private String sourceReserve;
    private BigDecimal price;
    private BigDecimal changedPrice;
    private Integer clientId;
    private Integer countPersons;
    private String adminComment;
    private String clientComment;
    private String historyMessages;

    public ReservationRedis(Reservation reservation) {
        this.id = reservation.getId();
        this.timeToLive = RedisCalendar.getTimeToLive(reservation.getDateReserve());
        this.dateReserve = reservation.getDateReserve();
        this.timeReserve = reservation.getTimeReserve();
        this.questId = reservation.getQuest().getId();
        this.dateAndTimeCreated = reservation.getDateAndTimeCreated();
        this.timeLastChange = reservation.getTimeLastChange();
        this.changedSlotTime = reservation.getChangedSlotTime();
        this.statusType = reservation.getStatusType();
        this.sourceReserve = reservation.getSourceReserve();
        this.price = reservation.getPrice();
        this.changedPrice = reservation.getChangedPrice();
        this.clientId = reservation.getClientId();
        this.countPersons = reservation.getCountPersons();
        this.adminComment = reservation.getAdminComment();
        this.clientComment = reservation.getClientComment();
        this.historyMessages = reservation.getHistoryMessages();
    }
}
