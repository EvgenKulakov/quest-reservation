package ru.questsfera.questreservation.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.dto.AccountDTO;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.service.company.CompanyService;

@Service
public class AccountParser {

    @Autowired
    private CompanyService companyService;

    public Account convertToEntity(AccountDTO accountDTO) {
        return new Account(
                accountDTO.getId(),
                accountDTO.getEmailLogin(),
                accountDTO.getPassword(),
                accountDTO.getFirstName(),
                accountDTO.getLastName(),
                accountDTO.getPhone(),
                accountDTO.getRole(),
                accountDTO.getCompany().getId(),
                accountDTO.getQuests()
        );
    }

    public AccountDTO convertToDTO(Account account) {
        return new AccountDTO(
                account.getId(),
                account.getEmailLogin(),
                account.getPassword(),
                account.getFirstName(),
                account.getLastName(),
                account.getPhone(),
                account.getRole(),
                companyService.findById(account.getCompanyId()),
                account.getQuests()
        );
    }
}
