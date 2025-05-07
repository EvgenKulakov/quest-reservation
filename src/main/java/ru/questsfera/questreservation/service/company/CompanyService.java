package ru.questsfera.questreservation.service.company;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.model.entity.Company;
import ru.questsfera.questreservation.repository.jpa.CompanyRepository;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public void saveCompany(Company company) {
        companyRepository.save(company);
    }
}
