package ru.questsfera.quest_reservation.processor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.questsfera.quest_reservation.model.dto.StatusType;

@Component
public class StatusTypeConverter implements Converter<String, StatusType> {

    @Override
    public StatusType convert(String source) {
        return StatusType.valueOf(source.toUpperCase());
    }
}

