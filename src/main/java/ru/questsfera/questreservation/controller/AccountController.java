package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.processor.PasswordGenerator;
import ru.questsfera.questreservation.service.AccountService;
import ru.questsfera.questreservation.service.QuestService;
import ru.questsfera.questreservation.validator.Patterns;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private QuestService questService;

    @GetMapping("/")
    public String showAccountsList(Principal principal, Model model) {

        Account account = accountService.getAccountByLogin(principal.getName());
        List<Account> accounts = accountService.getAccountsByCompany(account.getCompany());

        model.addAttribute("accounts", accounts);
        return "accounts/accounts-list";
    }

    @GetMapping("/add-form")
    public String addAccount(Principal principal, Model model) {

        Company company = accountService.getAccountByLogin(principal.getName()).getCompany();
        List<Quest> allQuests = questService.getQuestsByCompany(company);

        Account newAccount = new Account();
        newAccount.setCompany(company);
        newAccount.setPassword(PasswordGenerator.createRandomPassword());

        Account.Role[] roles = {Account.Role.ROLE_USER, Account.Role.ROLE_ADMIN};

        model.addAttribute("account", newAccount);
        model.addAttribute("all_quests", allQuests);
        model.addAttribute("roles", roles);
        return "accounts/account-form";
    }

    @PostMapping("/update-form")
    public String updateUser(@RequestParam("account") Account account, Model model) {

//        Admin admin = adminService.getAdminByName(principal.getName());
//        userService.checkSecurityForUser(user, admin);

        List<Quest> allQuests = questService.getQuestsByCompany(account.getCompany());
        Account.Role[] roles = {Account.Role.ROLE_USER, Account.Role.ROLE_ADMIN};

        model.addAttribute("account", account);
        model.addAttribute("all_quests", allQuests);
        model.addAttribute("roles", roles);
        return "accounts/account-form";
    }

    @PostMapping("/save-account")
    public String saveUser(@Valid @ModelAttribute("account") Account account,
                           BindingResult bindingResult,
                           Model model) {
//        Admin admin = adminService.getAdminByName(principal.getName());
//        user.setAdmin(admin);
//
//        if (user.getId() != null) userService.checkSecurityForUser(user, admin);

        boolean existsUsername = accountService.existAccountByLogin(account.getEmailLogin()) && account.getId() == null;

        if (bindingResult.hasErrors() || existsUsername) {

            if (existsUsername) {
                String errorMessage = "Аккаунт " + account.getEmailLogin() + " уже существует";
                bindingResult.rejectValue("emailLogin", "errorCode", errorMessage);
            }

            model.addAttribute("account", account);
            return "accounts/account-form";
        }

        if (account.getId() == null) {
            String passwordHash = PasswordGenerator.createBCrypt(account.getPassword());
            account.setPassword(passwordHash);
        }

        for (Quest quest : account.getQuests()) {
            quest.getAccounts().add(account);
            questService.saveQuest(quest);
        }

        accountService.saveAccount(account);
        return "redirect:/accounts/";
    }

    @PostMapping("/update-account-password")
    public String updatePassword(@RequestParam("account") Account account, Model model) {

//        Admin admin = adminService.getAdminByName(principal.getName());
//        userService.checkSecurityForUser(user, admin);

        model.addAttribute("account", account);
        model.addAttribute("new_password", "");
        model.addAttribute("error_password", false);
        return "accounts/password-form";
    }

    @PostMapping("/save-new-password")
    public String saveNewPassword(@RequestParam("account") Account account, Model model,
                                  @RequestParam("new_password") String newPassword) {

//        Admin admin = adminService.getAdminByName(principal.getName());
//        userService.checkSecurityForUser(user, admin);

        if (!newPassword.matches(Patterns.PASSWORD)) {
            model.addAttribute("account", account);
            model.addAttribute("new_password", newPassword);
            model.addAttribute("error_password", true);
            return "accounts/password-form";
        }

        account.setPassword(PasswordGenerator.createBCrypt(newPassword));
        accountService.saveAccount(account);
        return "redirect:/accounts/";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("account") Account account) {
        accountService.deleteAccount(account);
        return "redirect:/accounts/";
    }
}
