package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.mapper.AccountMapper;
import ru.questsfera.questreservation.model.dto.AccountCreateForm;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Company;
import ru.questsfera.questreservation.processor.PasswordGenerator;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.company.CompanyService;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AccountService accountService;
    private final CompanyService companyService;
    private final AccountMapper accountMapper;

    @RequestMapping("/login")
    public String login() {
        return "home/login";
    }

    @PostMapping("/register")
    public String register(Model model) {
        model.addAttribute("account", new AccountCreateForm());
        return "home/register";
    }

    @PostMapping("/register/save-new-account")
    public String saveNewAccount(@Valid @ModelAttribute("account") AccountCreateForm accountCreateForm,
                                 BindingResult bindingResult,
                                 @RequestParam("duplicate-pass") String duplicatePass,
                                 Model model) {

        if (accountService.existAccountByLogin(accountCreateForm.getLogin())) {
            bindingResult.rejectValue("login", "errorCode",
                    "*Такой пользователь уже зарегистрирован");
        }

        if (accountCreateForm.getCompany().getName().isBlank()) {
            bindingResult.rejectValue("company.name", "errorCode",
                    "*Введите название компании");
        }

        if (!accountCreateForm.getPassword().equals(duplicatePass)) {
            bindingResult.rejectValue("password", "errorCode",
                    "*Повторный пароль не совпадает");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("account", accountCreateForm);
            return "home/register";
        }

        Company company = accountCreateForm.getCompany();
        company.setMoney(new BigDecimal("10000.00")); //TODO default
        companyService.saveCompany(company);

        String passwordHash = PasswordGenerator.createBCrypt(accountCreateForm.getPassword());
        accountCreateForm.setPassword(passwordHash);

        accountCreateForm.setRole(Account.Role.ROLE_OWNER);

        accountService.saveAccount(accountMapper.toEntity(accountCreateForm));
        return "redirect:/login?new_account";
    }

    @GetMapping("/")
    public String homePage() {
        return "redirect:/reservations/slot-list";
    }
}
