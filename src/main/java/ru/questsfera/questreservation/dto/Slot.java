package ru.questsfera.questreservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.entity.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Slot {
    private Integer questId;
    private String questName;
    private Long reservationId;
    @JsonFormat(pattern = "dd-MM-yyyy (EEEE)", locale = "ru")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private Integer price; // TODO BigDecimal
    private List<Status> statuses;
    private StatusType statusType;
    private Integer minPersons;
    private Integer maxPersons;

    public Slot(QuestDTO quest, ReservationDTO reservation, LocalDate date, LocalTime time, Integer price) {
        this.questId = quest.getId();
        this.questName = quest.getQuestName();
        this.reservationId = reservation.getId();
        this.date = date;
        this.time = time;
        this.price = price;
        this.statuses = quest.getStatuses();
        this.statusType = reservation.getStatusType();
        this.minPersons = quest.getMinPersons();
        this.maxPersons = quest.getMaxPersons();
    }

    public Slot(QuestDTO quest, LocalDate date, LocalTime time, Integer price) {
        this.questId = quest.getId();
        this.questName = quest.getQuestName();
        this.date = date;
        this.time = time;
        this.price = price;
        this.statuses = quest.getStatuses();
        this.statusType = StatusType.EMPTY;
        this.minPersons = quest.getMinPersons();
        this.maxPersons = quest.getMaxPersons();
    }

    @JsonIgnore
    public String getJson() {
        return SlotMapper.createJSONSlot(this);
    }
}
