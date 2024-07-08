package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.entity.Blacklist;

public interface BlackListRepository extends JpaRepository<Blacklist, Integer> {
}