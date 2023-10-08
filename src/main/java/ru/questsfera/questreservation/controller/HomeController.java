package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.processor.PasswordGenerator;
import ru.questsfera.questreservation.service.AccountService;
import ru.questsfera.questreservation.service.AdminService;

@Controller
public class HomeController {
    private final AccountService accountService;
    private final AdminService adminService;

    @Autowired
    public HomeController(AccountService accountService, AdminService adminService) {
        this.accountService = accountService;
        this.adminService = adminService;
    }

    @RequestMapping("/login")
    public String login() {
        return "home/login";
    }

    @PostMapping("/register")
    public String register(Model model) {
        model.addAttribute("admin", new Admin());
        return "home/register";
    }

    @PostMapping("/register/save-new-account")
    public String saveNewAccount(@Valid @ModelAttribute("admin") Admin admin,
                                 BindingResult bindingResult,
                                 @RequestParam("duplicate-pass") String duplicatePass,
                                 Model model) {

        if (accountService.existAccount(admin.getEmail())) {
            bindingResult.rejectValue("email", "errorCode",
                    "Такой пользователь уже зарегистрирован");
            model.addAttribute("admin", admin);
            return "home/register";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("admin", admin);
            return "home/register";
        }

        if (!admin.getPassword().equals(duplicatePass)) {
            bindingResult.rejectValue("password", "errorCode",
                    "Повторный пароль не совпадает");
            model.addAttribute("admin", admin);
            return "home/register";
        }

        admin.setUsername(admin.getEmail());
        String passwordHash = PasswordGenerator.createBCrypt(admin.getPassword());
        admin.setPassword(passwordHash);

        adminService.saveAdmin(admin);

        return "redirect:/login?new_account";
    }

    @GetMapping("/")
    public String homePage() {
        return "redirect:/reservations/slot-list";
    }
}
