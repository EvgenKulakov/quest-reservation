package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}