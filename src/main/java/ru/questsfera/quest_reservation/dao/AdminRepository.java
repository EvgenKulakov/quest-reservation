package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
