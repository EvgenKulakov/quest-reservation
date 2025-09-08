package ru.questsfera.questreservation.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.service.quest.QuestService;

@Component
@RequiredArgsConstructor
public class QuestConverter implements Converter<String, Quest> {

    private final QuestService questService;

    @Override
    public Quest convert(String source) {
        if (source.isBlank()) return null;
        Integer id = Integer.parseInt(source);
        return questService.findById(id);
    }
}
