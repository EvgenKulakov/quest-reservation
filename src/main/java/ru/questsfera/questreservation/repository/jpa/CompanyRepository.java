package ru.questsfera.questreservation.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
