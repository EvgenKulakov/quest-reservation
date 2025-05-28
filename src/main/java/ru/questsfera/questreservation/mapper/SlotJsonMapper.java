package ru.questsfera.questreservation.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.questsfera.questreservation.model.dto.Slot;

public class SlotJsonMapper {
    private static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public static Slot createSlotObject(String jsonSlot) {
        try {
            return mapper.readValue(jsonSlot, Slot.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createJSONSlot(Slot slot) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(slot);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
