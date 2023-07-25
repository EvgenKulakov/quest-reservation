package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}