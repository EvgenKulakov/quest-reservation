package ru.questsfera.questreservation.model.dto;

import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Value
public class SlotListPage {
    Map<String, List<Slot>> questNamesAndSlots;
    Set<Status> useStatuses;

    public Optional<Slot> getSlotById(Integer slotId) {
        return questNamesAndSlots
                .values()
                .stream()
                .flatMap(List::stream)
                .filter(slot -> slot.getSlotId().equals(slotId))
                .findFirst();
    }

    public Optional<Slot> getSLotByDataAnotherSlot(Slot otherSlot) {
        return questNamesAndSlots
                .values()
                .stream()
                .flatMap(List::stream)
                .filter(slot -> equalsSlotData(slot, otherSlot))
                .findFirst();
    }

    private boolean equalsSlotData(Slot slot, Slot otherSlot) {
        return slot.getQuestId().equals(otherSlot.getQuestId())
                && slot.getDate().equals(otherSlot.getDate())
                && slot.getTime().equals(otherSlot.getTime());
    }
}
