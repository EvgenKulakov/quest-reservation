package ru.questsfera.questreservation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.questsfera.questreservation.model.dto.QuestDTO;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.model.entity.Status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestMapper {
    @Mapping(target = "statuses", expression = "java(mapStatuses(quest.getStatuses()))")
    QuestDTO toDto(Quest quest);

    default List<Status> mapStatuses(String statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(statuses.split(","))
                .map(Status::fromStatusTypeName)
                .toList();
    }
}
