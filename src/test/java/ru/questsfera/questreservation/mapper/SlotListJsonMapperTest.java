package ru.questsfera.questreservation.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.questsfera.questreservation.config.JacksonConfig;
import ru.questsfera.questreservation.model.dto.SlotList;
import ru.questsfera.questreservation.model.dto.TimePrice;

import java.time.LocalTime;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SlotListJsonMapper.class, JacksonConfig.class})
class SlotListJsonMapperTest {

    @Autowired
    SlotListJsonMapper slotListJsonMapper;

    @Test
    void toObject_success() {
        String slotListJson = getSlotListJson();
        SlotList actual = slotListJsonMapper.toObject(slotListJson);
        SlotList excepted = getSLotListObject();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(excepted);
    }

    @Test
    void toObject_failure() {
        String wrongJson = "{\"someValue\": 1000}";
        assertThatThrownBy(() -> slotListJsonMapper.toObject(wrongJson))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(JsonProcessingException.class);
    }

    @Test
    void toJSON_success() {
        SlotList slotListObject = getSLotListObject();
        String actual = slotListJsonMapper.toJSON(slotListObject);
        String excepted = getSlotListJson();

        assertThatJson(actual).isEqualTo(excepted);
    }

    @Test
    void toJSON_failure() throws JsonProcessingException {
        ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
        ObjectWriter objectWriterMock = Mockito.mock(ObjectWriter.class);
        SlotListJsonMapper slotListJsonMapperMock = new SlotListJsonMapper(objectMapperMock);
        SlotList slotList = new SlotList();

        when(objectMapperMock.writerWithDefaultPrettyPrinter()).thenReturn(objectWriterMock);
        when(objectWriterMock.writeValueAsString(slotList)).thenThrow(JsonProcessingException.class);

        assertThatThrownBy(() -> slotListJsonMapperMock.toJSON(slotList))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(JsonProcessingException.class);
    }

    private String getSlotListJson() {
        return """
               {
                 "monday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "tuesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "wednesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "thursday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "friday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "saturday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ],
                 "sunday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000} ]
               }""";
    }

    private SlotList getSLotListObject() {
        List<TimePrice> timePriceList = List.of(
                new TimePrice(LocalTime.parse("12:00"), 3000),
                new TimePrice(LocalTime.parse("13:00"), 3000)
        );

        SlotList slotList = new SlotList();
        slotList.setMonday(timePriceList);
        slotList.setTuesday(timePriceList);
        slotList.setWednesday(timePriceList);
        slotList.setThursday(timePriceList);
        slotList.setFriday(timePriceList);
        slotList.setSaturday(timePriceList);
        slotList.setSunday(timePriceList);

        return slotList;
    }
}