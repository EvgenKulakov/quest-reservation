package ru.questsfera.questreservation.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.model.dto.Slot;

@Component
@RequiredArgsConstructor
public class SlotJsonMapper {
    private final ObjectMapper mapper;

    public Slot toObject(String jsonSlot) {
        try {
            return mapper.readValue(jsonSlot, Slot.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toJSON(Slot slot) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(slot);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
