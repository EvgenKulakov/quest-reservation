package ru.questsfera.questreservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.entity.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Slot {
    private Integer companyId;
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
        this.companyId = quest.getCompanyId();
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
        this.companyId = quest.getCompanyId();
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(companyId, slot.companyId)
                && Objects.equals(questId, slot.questId)
                && Objects.equals(questName, slot.questName)
                && Objects.equals(reservationId, slot.reservationId)
                && Objects.equals(date, slot.date)
                && Objects.equals(time, slot.time)
                && Objects.equals(price, slot.price)
                && Objects.equals(statuses, slot.statuses)
                && statusType == slot.statusType
                && Objects.equals(minPersons, slot.minPersons)
                && Objects.equals(maxPersons, slot.maxPersons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, questId, questName, reservationId, date,
                time, price, statuses, statusType, minPersons, maxPersons);
    }
}
