package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.User;
import ru.questsfera.questreservation.repository.*;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public List<User> getUsersByAdmin(Admin admin) {
        return userRepository.findAllByAdminOrderByUsername(admin);
    }

    @Transactional
    public boolean existUserByAdmin(User user, Admin admin) {
        return userRepository.existsUserByIdAndAdmin(user.getId(), admin);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User user, Admin admin) {
        checkSecurityForUser(user, admin);
        userRepository.delete(user);
    }

    @Transactional
    public void checkSecurityForUser(User user, Admin admin) {
        boolean existUserByAdmin = existUserByAdmin(user, admin);
        if (!existUserByAdmin) {
            throw new SecurityException("Нет доступа для изменения данного пользователя");
        }
    }

    @Transactional
    public void checkSecurityForUsers(Set<User> users, Admin admin) {
        List<User> usersByAdmin = getUsersByAdmin(admin);
        if (!usersByAdmin.containsAll(users)) {
            throw new SecurityException("Нет доступа для изменения данных пользователей");
        }
    }
}
