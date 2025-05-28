package ru.questsfera.questreservation.mapper;

import org.mapstruct.Mapper;
import ru.questsfera.questreservation.model.dto.QuestDTO;
import ru.questsfera.questreservation.model.entity.Quest;

@Mapper(componentModel = "spring")
public interface QuestMapper {
    QuestDTO toDto(Quest quest);
}
