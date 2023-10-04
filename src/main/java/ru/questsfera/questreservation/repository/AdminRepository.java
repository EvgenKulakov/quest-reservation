package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findAdminByUsername(String username);
}
