package ru.questsfera.questreservation.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.mapper.SlotJsonMapper;
import ru.questsfera.questreservation.model.entity.Status;

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

    public static Slot fromQuestDateReservationPrice(QuestDTO questDTO, LocalDate date, ReservationDTO reservationDTO, Integer price) {
        Slot slot = new Slot();
        slot.setCompanyId(questDTO.getCompanyId());
        slot.setQuestId(questDTO.getId());
        slot.setQuestName(questDTO.getQuestName());
        slot.setReservationId(reservationDTO.getId());
        slot.setDate(date);
        slot.setTime(reservationDTO.getTimeReserve());
        slot.setPrice(price);
        slot.setStatuses(questDTO.getStatuses());
        slot.setStatusType(reservationDTO.getStatusType());
        slot.setMinPersons(questDTO.getMinPersons());
        slot.setMaxPersons(questDTO.getMaxPersons());
        return slot;
    }

    public static Slot emptyFromQuestDateTimePrice(QuestDTO questDTO, LocalDate date, LocalTime time, Integer price) {
        Slot slot = new Slot();
        slot.setCompanyId(questDTO.getCompanyId());
        slot.setQuestId(questDTO.getId());
        slot.setQuestName(questDTO.getQuestName());
        slot.setDate(date);
        slot.setTime(time);
        slot.setPrice(price);
        slot.setStatuses(questDTO.getStatuses());
        slot.setStatusType(StatusType.EMPTY);
        slot.setMinPersons(questDTO.getMinPersons());
        slot.setMaxPersons(questDTO.getMaxPersons());
        return slot;
    }

    @JsonIgnore
    public String getJson() {
        return SlotJsonMapper.createJSONSlot(this);
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
                && Objects.equals(statusType, slot.statusType)
                && Objects.equals(minPersons, slot.minPersons)
                && Objects.equals(maxPersons, slot.maxPersons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, questId, questName, reservationId, date,
                time, price, statuses, statusType, minPersons, maxPersons);
    }
}
