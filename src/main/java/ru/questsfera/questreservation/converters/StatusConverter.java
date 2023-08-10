package ru.questsfera.questreservation.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.dao.StatusRepository;
import ru.questsfera.questreservation.entity.Status;

import java.util.Optional;

@Component
public class StatusConverter implements Converter<String, Status> {
    private final StatusRepository statusRepository;

    @Autowired
    public StatusConverter(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Status convert(String source) {
        Integer id = Integer.parseInt(source);

        Optional<Status> statusOptional = statusRepository.findById(id);
        if (statusOptional.isPresent()) {
            return statusOptional.get();
        }
        throw new RuntimeException("Ошибка с конвертером статуса");
    }
}

