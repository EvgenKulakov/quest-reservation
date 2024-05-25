package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.redis.object.AccountRedis;
import ru.questsfera.questreservation.redis.service.AccountRedisService;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.processor.PasswordGenerator;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.company.CompanyService;
import ru.questsfera.questreservation.service.quest.QuestService;
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
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AccountRedisService accountRedisService;


    @GetMapping("/")
    public String showAccountsList(Principal principal, Model model) {

        Account account = accountService.getAccountByLogin(principal.getName());
        List<Account> accounts = accountService.getAccountsByCompany(account.getCompany());
        if (account.getRole() != Account.Role.ROLE_OWNER) {
             accounts.removeIf(acc -> !acc.getRole().equals(Account.Role.ROLE_USER));
        }

        model.addAttribute("accounts", accounts);
        model.addAttribute("myAccount", account);
        return "accounts/accounts-list";
    }

    @GetMapping("/add-form")
    public String addAccount(Principal principal, Model model) {

        AccountRedis myAccount = accountRedisService.findByEmailLogin(principal.getName());
        Company company = companyService.findById(myAccount.getCompanyId());
        List<Quest> allQuests = questService.getQuestsByCompany(company);

        Account newAccount = new Account();
        newAccount.setCompany(company);
        newAccount.setPassword(PasswordGenerator.createRandomPassword());

        Account.Role[] roles = {Account.Role.ROLE_USER, Account.Role.ROLE_ADMIN};

        model.addAttribute("account", newAccount);
        model.addAttribute("allQuests", allQuests);
        model.addAttribute("roles", roles);
        return "accounts/account-form";
    }

    @PostMapping("/update-form")
    public String updateAccount(@RequestParam("account") Account account, Principal principal, Model model) {

        AccountRedis myAccount = accountRedisService.findByEmailLogin(principal.getName());
        accountService.checkSecurityForAccount(account, myAccount);

        List<Quest> allQuests = questService.getQuestsByCompany(account.getCompany());
        Account.Role[] roles = {Account.Role.ROLE_USER, Account.Role.ROLE_ADMIN};

        model.addAttribute("account", account);
        model.addAttribute("allQuests", allQuests);
        model.addAttribute("roles", roles);
        return "accounts/account-form";
    }

    @PostMapping("/save-account")
    public String saveAccount(@Valid @ModelAttribute("account") Account account,
                              BindingResult bindingResult,
                              @RequestParam("oldLogin") String oldLogin,
                              Principal principal,
                              Model model) {

        AccountRedis myAccount = accountRedisService.findByEmailLogin(principal.getName());
        if (account.getId() != null) accountService.checkSecurityForAccount(account, myAccount);

        if (!account.getEmailLogin().equals(oldLogin)) {
            boolean existsUsername = accountRedisService.existByEmailLogin(account.getEmailLogin());
            if (existsUsername) {
                String errorMessage = "Аккаунт " + account.getEmailLogin() + " уже существует";
                bindingResult.rejectValue("emailLogin", "errorCode", errorMessage);
            } else if (account.getId() != null) {
                accountRedisService.deleteByEmailLogin(oldLogin);
            }
        }

        if (bindingResult.hasErrors()) {
            List<Quest> allQuests = questService.getQuestsByCompany(account.getCompany());
            Account.Role[] roles = {Account.Role.ROLE_USER, Account.Role.ROLE_ADMIN};

            model.addAttribute("account", account);
            model.addAttribute("allQuests", allQuests);
            model.addAttribute("roles", roles);
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
    public String updatePassword(@RequestParam("account") Account account, Principal principal, Model model) {

        AccountRedis myAccount = accountRedisService.findByEmailLogin(principal.getName());
        accountService.checkSecurityForAccount(account, myAccount);

        model.addAttribute("account", account);
        model.addAttribute("newPassword", "");
        model.addAttribute("errorPassword", false);
        return "accounts/password-form";
    }

    @PostMapping("/save-new-password")
    public String saveNewPassword(@RequestParam("account") Account account,
                                  @RequestParam("newPassword") String newPassword,
                                  Principal principal, Model model) {

        AccountRedis myAccount = accountRedisService.findByEmailLogin(principal.getName());
        accountService.checkSecurityForAccount(account, myAccount);

        //TODO: safe update password: *********
        if (!newPassword.matches(Patterns.PASSWORD)) {
            model.addAttribute("account", account);
            model.addAttribute("newPassword", newPassword);
            model.addAttribute("errorPassword", true);
            return "accounts/password-form";
        }

        account.setPassword(PasswordGenerator.createBCrypt(newPassword));
        accountService.saveAccount(account);
        return "redirect:/accounts/";
    }

    @PostMapping("/delete")
    public String deleteAccount(@RequestParam("account") Account account, Principal principal) {
        AccountRedis myAccount = accountRedisService.findByEmailLogin(principal.getName());
        accountService.checkSecurityForAccount(account, myAccount);
        accountService.delete(account);
        return "redirect:/accounts/";
    }
}
