package ru.questsfera.quest_reservation.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.questsfera.quest_reservation.dto.StatusType;

@Component
public class StatusTypeConverter implements Converter<String, StatusType> {

    @Override
    public StatusType convert(String source) {
        return StatusType.valueOf(source.toUpperCase());
    }
}

