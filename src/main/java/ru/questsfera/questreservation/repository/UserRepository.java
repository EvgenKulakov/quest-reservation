package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsUserByUsernameAndAdmin(String username, Admin admin);

    List<User> findAllByAdminOrderByUsername(Admin admin);
}