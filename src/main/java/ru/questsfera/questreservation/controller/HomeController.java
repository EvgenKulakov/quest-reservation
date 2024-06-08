package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.converter.AccountParser;
import ru.questsfera.questreservation.dto.AccountDTO;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.processor.PasswordGenerator;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.company.CompanyService;

import java.math.BigDecimal;

@Controller
public class HomeController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountParser accountParser;
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/login")
    public String login() {
        return "home/login";
    }

    @PostMapping("/register")
    public String register(Model model) {
        model.addAttribute("account", new AccountDTO());
        return "home/register";
    }

    @PostMapping("/register/save-new-account")
    public String saveNewAccount(@Valid @ModelAttribute("account") AccountDTO accountDTO,
                                 BindingResult bindingResult,
                                 @RequestParam("duplicate-pass") String duplicatePass,
                                 Model model) {

        if (accountService.existAccountByLogin(accountDTO.getEmailLogin())) {
            bindingResult.rejectValue("emailLogin", "errorCode",
                    "*Такой пользователь уже зарегистрирован");
        }

        if (accountDTO.getCompany().getName().isBlank()) {
            bindingResult.rejectValue("company.name", "errorCode",
                    "*Введите название компании");
        }

        if (!accountDTO.getPassword().equals(duplicatePass)) {
            bindingResult.rejectValue("password", "errorCode",
                    "*Повторный пароль не совпадает");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("account", accountDTO);
            return "home/register";
        }

        Company company = accountDTO.getCompany();
        company.setMoney(new BigDecimal("10000.00")); // TODO: default
        companyService.saveCompany(company);

        String passwordHash = PasswordGenerator.createBCrypt(accountDTO.getPassword());
        accountDTO.setPassword(passwordHash);

        accountDTO.setRole(Account.Role.ROLE_OWNER);

        Account account = accountParser.convertToEntity(accountDTO);

        accountService.saveAccount(account);
        return "redirect:/login?new_account";
    }

    @GetMapping("/")
    public String homePage() {
        return "redirect:/reservations/slot-list";
    }
}
