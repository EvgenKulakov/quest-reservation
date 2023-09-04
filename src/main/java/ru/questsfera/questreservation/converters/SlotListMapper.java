package ru.questsfera.questreservation.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.questsfera.questreservation.dto.SlotList;

import java.io.IOException;
import java.time.LocalTime;

public class SlotListMapper {

    public static SlotList createSlotListObject(String jsonSlotList) {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addKeyDeserializer(LocalTime.class, new KeyDeserializer() {
            @Override
            public LocalTime deserializeKey(String s, DeserializationContext deserializationContext) {
                return LocalTime.parse(s);
            }
        });
        mapper.registerModule(simpleModule);

        SlotList slotList = null;
        try {
            slotList = mapper.readValue(jsonSlotList, SlotList.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return slotList;
    }

    public static String createJSONSlotList(SlotList slotList) {
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
