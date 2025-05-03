package ru.questsfera.questreservation.service.company;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.repository.jpa.CompanyRepository;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock CompanyRepository companyRepository;
    @InjectMocks CompanyService companyService;

    @Test
    void saveCompany() {
        Company company = Mockito.mock(Company.class);
        companyService.saveCompany(company);
        verify(companyRepository).save(company);
    }
}