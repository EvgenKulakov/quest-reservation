package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.processor.PasswordGenerator;
import ru.questsfera.questreservation.service.AccountService;
import ru.questsfera.questreservation.service.CompanyService;

@Controller
public class HomeController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/login")
    public String login() {
        return "home/login";
    }

    @PostMapping("/register")
    public String register(Model model) {
        model.addAttribute("account", new Account());
        return "home/register";
    }

    @PostMapping("/register/save-new-account")
    public String saveNewAccount(@Valid @ModelAttribute("account") Account account,
                                 BindingResult bindingResult,
                                 @RequestParam("duplicate-pass") String duplicatePass,
                                 Model model) {

        if (accountService.existAccountByLogin(account.getEmailLogin())) {
            bindingResult.rejectValue("email", "errorCode",
                    "Такой пользователь уже зарегистрирован");
            model.addAttribute("account", account);
            return "home/register";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("account", account);
            return "home/register";
        }

        if (!account.getPassword().equals(duplicatePass)) {
            bindingResult.rejectValue("password", "errorCode",
                    "Повторный пароль не совпадает");
            model.addAttribute("account", account);
            return "home/register";
        }

        String passwordHash = PasswordGenerator.createBCrypt(account.getPassword());

        account.setPassword(passwordHash);
        account.setRole(Account.Role.ROLE_OWNER);
        account.getCompany().setOwnerId(account.getId());

        companyService.saveCompany(account.getCompany());
        accountService.saveAccount(account);

        return "redirect:/login?new_account";
    }

    @GetMapping("/")
    public String homePage() {
        return "redirect:/reservations/slot-list";
    }
}
