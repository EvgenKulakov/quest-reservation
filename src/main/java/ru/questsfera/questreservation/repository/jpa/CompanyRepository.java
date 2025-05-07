package ru.questsfera.questreservation.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.questsfera.questreservation.model.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
