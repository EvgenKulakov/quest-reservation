package ru.questsfera.quest_reservation.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SlotListMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    public SlotList createSlotList(String jsonSlotList) {
        ObjectMapper mapper = new ObjectMapper();

        SlotList slotList = null;
        try {
            slotList = mapper.readValue(jsonSlotList, SlotList.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return slotList;
    }

    public String createJSONSlotList(SlotList slotList) {
        ObjectMapper mapper = new ObjectMapper();

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
