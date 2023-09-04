package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Status;

import java.util.Set;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    public Set<Status> findStatusesByTypeIn(Set<StatusType> statusTypes);
}