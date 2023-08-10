package ru.questsfera.questreservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Integer> {
}