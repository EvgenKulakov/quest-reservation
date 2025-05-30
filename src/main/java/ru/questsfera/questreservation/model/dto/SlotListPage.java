package ru.questsfera.questreservation.model.dto;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Value
public class SlotListPage {
    Map<String, List<Slot>> questNamesAndSlots;
    Set<Status> useStatuses;
}
