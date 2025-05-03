package ru.questsfera.questreservation.dto;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Value
public class SlotListPageDTO {
    Map<String, List<Slot>> questNamesAndSlots;
    Set<StatusType> useStatuses;
}
