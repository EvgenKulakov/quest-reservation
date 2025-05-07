package ru.questsfera.questreservation.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.model.entity.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Integer> {
}