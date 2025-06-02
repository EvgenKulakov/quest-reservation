package ru.questsfera.questreservation.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.model.dto.SlotList;

@Component
@RequiredArgsConstructor
public class SlotListJsonMapper {
    private final ObjectMapper mapper;

    public SlotList toObject(String jsonSlotList) {
        try {
            return mapper.readValue(jsonSlotList, SlotList.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toJSON(SlotList slotList) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(slotList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
