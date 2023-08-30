package ru.questsfera.questreservation.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.repository.UserRepository;
import ru.questsfera.questreservation.entity.User;

import java.util.Optional;

@Component
public class UserConverter implements Converter<String, User> {

    private final UserRepository userRepository;

    @Autowired
    public UserConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User convert(String source) {
        Integer id = Integer.parseInt(source);

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new RuntimeException("Ошибка при конвертации объекта User");
    }
}
