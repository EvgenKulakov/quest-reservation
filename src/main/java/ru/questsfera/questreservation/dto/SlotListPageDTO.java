package ru.questsfera.questreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotListPageDTO {
    private Map<String, List<Slot>> questsAndSlots;
    private Set<StatusType> useStatuses;
}
