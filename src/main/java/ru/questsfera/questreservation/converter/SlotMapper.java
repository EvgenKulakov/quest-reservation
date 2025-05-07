package ru.questsfera.questreservation.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.questsfera.questreservation.model.dto.Slot;

public class SlotMapper {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
    }

    public static Slot createSlotObject(String jsonSlot) {

        Slot slot = null;
        try {
            slot = mapper.readValue(jsonSlot, Slot.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return slot;
    }

    public static String createJSONSlot(Slot slot) {

        String JSONSlot = null;
        try {
            JSONSlot = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(slot);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return JSONSlot;
    }
}
