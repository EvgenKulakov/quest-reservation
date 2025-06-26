package ru.questsfera.questreservation.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.model.entity.Quest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Slot {
    private Integer slotId;
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
    private Status status;
    private Integer minPersons;
    private Integer maxPersons;

    public static Slot withReserve(Integer slotId, Quest quest, LocalDate date, ReservationWIthClient reservationWIthClient, Integer price) {
        Slot slot = new Slot();
        slot.setSlotId(slotId);
        slot.setCompanyId(quest.getCompanyId());
        slot.setQuestId(quest.getId());
        slot.setQuestName(quest.getQuestName());
        slot.setReservationId(reservationWIthClient.getId());
        slot.setDate(date);
        slot.setTime(reservationWIthClient.getTimeReserve());
        slot.setPrice(price);
        slot.setStatuses(quest.getStatuses());
        slot.setStatus(reservationWIthClient.getStatus());
        slot.setMinPersons(quest.getMinPersons());
        slot.setMaxPersons(quest.getMaxPersons());
        return slot;
    }

    public static Slot empty(Integer slotId, Quest quest, LocalDate date, LocalTime time, Integer price) {
        Slot slot = new Slot();
        slot.setSlotId(slotId);
        slot.setCompanyId(quest.getCompanyId());
        slot.setQuestId(quest.getId());
        slot.setQuestName(quest.getQuestName());
        slot.setDate(date);
        slot.setTime(time);
        slot.setPrice(price);
        slot.setStatuses(quest.getStatuses());
        slot.setStatus(Status.EMPTY);
        slot.setMinPersons(quest.getMinPersons());
        slot.setMaxPersons(quest.getMaxPersons());
        return slot;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(slotId, slot.slotId)
                && Objects.equals(companyId, slot.companyId)
                && Objects.equals(questId, slot.questId)
                && Objects.equals(questName, slot.questName)
                && Objects.equals(reservationId, slot.reservationId)
                && Objects.equals(date, slot.date)
                && Objects.equals(time, slot.time)
                && Objects.equals(price, slot.price)
                && Objects.equals(statuses, slot.statuses)
                && Objects.equals(status, slot.status)
                && Objects.equals(minPersons, slot.minPersons)
                && Objects.equals(maxPersons, slot.maxPersons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slotId, companyId, questId, questName, reservationId,
                date, time, price, statuses, status, minPersons, maxPersons);
    }
}
