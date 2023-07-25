package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.entity.AdminEntity;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
}
