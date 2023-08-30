package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}