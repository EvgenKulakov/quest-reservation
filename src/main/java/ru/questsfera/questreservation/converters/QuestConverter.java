package ru.questsfera.questreservation.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.dao.QuestRepository;
import ru.questsfera.questreservation.entity.Quest;

import java.util.Optional;

@Component
public class QuestConverter implements Converter<String, Quest> {
    private final QuestRepository questRepository;

    @Autowired
    public QuestConverter(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    @Override
    public Quest convert(String source) {
        Integer id = Integer.parseInt(source);

        Optional<Quest> optionalQuest = questRepository.findById(id);
        if (optionalQuest.isPresent()) {
            return optionalQuest.get();
        }
        throw new RuntimeException("Ошибка при конвертации объекта Quest");
    }
}
