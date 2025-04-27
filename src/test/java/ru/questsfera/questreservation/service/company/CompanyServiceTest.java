package ru.questsfera.questreservation.service.company;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.repository.jpa.CompanyRepository;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock CompanyRepository companyRepository;
    @InjectMocks CompanyService companyService;

    @Test
    void saveCompany() {
        Company company = getCompany();
        companyService.saveCompany(company);
        verify(companyRepository).save(company);
    }

    private Company getCompany() {
        return new Company(1, "test company", new BigDecimal("10000.00"));
    }
}