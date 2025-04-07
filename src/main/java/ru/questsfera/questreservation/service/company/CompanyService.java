package ru.questsfera.questreservation.service.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.repository.jpa.CompanyRepository;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public void saveCompany(Company company) {
        companyRepository.save(company);
    }

    public Company findById(Integer id) {
        return companyRepository.findById(id).orElse(null);
    }
}
