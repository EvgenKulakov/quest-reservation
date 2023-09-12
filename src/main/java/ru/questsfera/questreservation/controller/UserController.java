package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.User;
import ru.questsfera.questreservation.processor.PasswordGenerator;
import ru.questsfera.questreservation.service.AdminService;

import java.util.Set;

@Controller
public class UserController {

    private final AdminService adminService;
    private Admin admin;

    @Autowired
    public UserController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/user-list")
    public String showUserList(Model model) {
        /* test admin account */
        admin = adminService.getAdminById(1);
        Set<User> users = adminService.getUsersByAdmin(admin);
        model.addAttribute("admin", admin);
        model.addAttribute("users", users);
        return "user-list-page";
    }

    @PostMapping("/add-user")
    public String addUser(@RequestParam("admin") Admin admin, Model model) {
        User user = new User(admin);
        user.setPasswordHash(PasswordGenerator.createRandomPassword());
        model.addAttribute("user", user);
        return "add-user-form";
    }

    @PostMapping("/save-user")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "add-user-form";
        }

        PasswordGenerator.createBCrypt(user);
        adminService.saveUser(user);
        return "redirect:/user-list";
    }
}
