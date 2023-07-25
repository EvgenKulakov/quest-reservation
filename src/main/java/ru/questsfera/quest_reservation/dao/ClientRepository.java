package ru.questsfera.quest_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.quest_reservation.entity.ClientEntity;

public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
}