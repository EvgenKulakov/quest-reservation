package ru.questsfera.questreservation.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.model.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}