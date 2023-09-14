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
import ru.questsfera.questreservation.validator.Patterns;

import java.util.List;

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

        List<User> users = adminService.getUsersByAdmin(admin);

        model.addAttribute("admin", admin);
        model.addAttribute("users", users);

        return "users/user-list-page";
    }

    @PostMapping("/add-user")
    public String addUser(@RequestParam("admin") Admin admin, Model model) {
        User user = new User(admin);
        user.setPasswordHash(PasswordGenerator.createRandomPassword());

        model.addAttribute("user", user);
        return "users/add-user-form";
    }

    @PostMapping("/save-user")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           Model model) {
        boolean existsUsername = adminService.existsUsername(user) && user.getId() == null;

        if (bindingResult.hasErrors() || existsUsername) {

            if (existsUsername) {
                String errorMessage = "Username " + user.getUsername() + " уже существует";
                bindingResult.rejectValue("username", "errorCode", errorMessage);
            }

            model.addAttribute("user", user);
            return "users/add-user-form";
        }

        if (user.getId() == null) {
            PasswordGenerator.createBCrypt(user);
        }

        adminService.saveUser(user);
        return "redirect:/user-list";
    }

    @PostMapping("/update-user")
    public String updateUser(@RequestParam("user") User user, Model model) {
        model.addAttribute("user", user);
        return "users/add-user-form";
    }

    @PostMapping("/update-password-user")
    public String updatePassword(@RequestParam("user") User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("new_password", "");
        model.addAttribute("error_password", false);
        return "users/update-password-form";
    }

    @PostMapping("/save-new-password")
    public String saveNewPassword(@RequestParam("user") User user, Model model,
                                  @RequestParam("new_password") String newPassword,
                                  @RequestParam("error_password") Boolean errorPassword) {

        if (!newPassword.matches(Patterns.PASSWORD)) {
            errorPassword = true;
            model.addAttribute("user", user);
            model.addAttribute("new_password", newPassword);
            model.addAttribute("error_password", errorPassword);
            return "users/update-password-form";
        }

        PasswordGenerator.createBCrypt(user);
        adminService.saveUser(user);
        return "redirect:/user-list";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("user") User user) {
        adminService.deleteUser(user);
        return "redirect:/user-list";
    }
}
