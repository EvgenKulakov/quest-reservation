package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}