package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.entity.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

}