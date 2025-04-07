package ru.questsfera.questreservation.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.questsfera.questreservation.dto.SlotList;

public class SlotListMapper {

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
    }

    public static SlotList createObject(String jsonSlotList) {

        SlotList slotList = null;
        try {
            slotList = mapper.readValue(jsonSlotList, SlotList.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return slotList;
    }

    public static String createJSON(SlotList slotList) {

        String JSONSlotList = null;
        try {
            JSONSlotList = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(slotList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return JSONSlotList;
    }
}
