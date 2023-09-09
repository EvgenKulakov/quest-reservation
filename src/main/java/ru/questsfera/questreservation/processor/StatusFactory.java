package ru.questsfera.questreservation.processor;

import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.StatusType;

import java.util.List;
import java.util.Set;

public class StatusFactory {

    public static void addUniqueStatusTypes(Set<StatusType> statusTypes, List<Slot> slots) {
        for (Slot slot : slots) {
            statusTypes.add(slot.getStatusType());
        }
    }
}
