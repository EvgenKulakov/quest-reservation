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
import ru.questsfera.questreservation.model.dto.Slot;
import ru.questsfera.questreservation.model.dto.Status;

import java.time.LocalDate;
import java.time.LocalTime;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SlotJsonMapper.class, JacksonConfig.class})
class SlotJsonMapperTest {

    @Autowired
    SlotJsonMapper slotJsonMapper;

    @Test
    void toObject_success() {
        String slotJson = getSLotJson();
        Slot actual = slotJsonMapper.toObject(slotJson);
        Slot excepted = getSLotObject();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(excepted);
    }

    @Test
    void toObject_failure() {
        String wrongJson = "{\"someJson\": 90}";
        assertThatThrownBy(() -> slotJsonMapper.toObject(wrongJson))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(JsonProcessingException.class);
    }

    @Test
    void toJSON_success() {
        Slot slotObject = getSLotObject();
        String actual = slotJsonMapper.toJSON(slotObject);
        String excepted = getSLotJson();

        assertThatJson(actual).isEqualTo(excepted);
    }

    @Test
    void toJSON_failure() throws JsonProcessingException {
        ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
        ObjectWriter mockWriter = Mockito.mock(ObjectWriter.class);
        SlotJsonMapper slotJsonMapperMock = new SlotJsonMapper(objectMapperMock);
        Slot slot = new Slot();

        when(objectMapperMock.writerWithDefaultPrettyPrinter()).thenReturn(mockWriter);
        when(mockWriter.writeValueAsString(slot)).thenThrow(JsonProcessingException.class);
        assertThatThrownBy(() -> slotJsonMapperMock.toJSON(slot))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(JsonProcessingException.class);
    }

    private Slot getSLotObject() {
        return new Slot(
                1,
                1,
                1,
                "Quest One",
                3L,
                LocalDate.of(2025, 4, 22),
                LocalTime.of(17, 0),
                3000,
                Status.DEFAULT_STATUSES,
                Status.NOT_COME,
                1,
                6
        );
    }

    private String getSLotJson() {
        return """
                {
                  "companyId" : 1,
                  "slotId" : 1,
                  "questId" : 1,
                  "questName" : "Quest One",
                  "reservationId" : 3,
                  "date" : "22-04-2025 (вторник)",
                  "time" : "17:00",
                  "price" : 3000,
                  "statuses" : [ {
                    "name" : "NEW_RESERVE",
                    "text" : "Новый"
                  }, {
                    "name" : "CANCEL",
                    "text" : "Отменён"
                  }, {
                    "name" : "CONFIRMED",
                    "text" : "Подтверждён"
                  }, {
                    "name" : "NOT_COME",
                    "text" : "Не пришёл"
                  }, {
                    "name" : "COMPLETED",
                    "text" : "Завершён"
                  } ],
                  "status" : {
                    "name" : "NOT_COME",
                    "text" : "Не пришёл"
                  },
                  "minPersons" : 1,
                  "maxPersons" : 6
                }""";
    }
}