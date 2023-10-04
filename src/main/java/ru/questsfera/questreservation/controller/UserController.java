package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.User;
import ru.questsfera.questreservation.processor.PasswordGenerator;
import ru.questsfera.questreservation.service.AdminService;
import ru.questsfera.questreservation.validator.Patterns;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final AdminService adminService;

    @Autowired
    public UserController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/user-list")
    public String showUserList(Principal principal, Model model) {

        Admin admin = adminService.getAdminByName(principal.getName());
        List<User> users = adminService.getUsersByAdmin(admin);

        model.addAttribute("admin", admin);
        model.addAttribute("users", users);

        return "users/user-list-page";
    }

    @PostMapping("/add-user")
    public String addUser(@RequestParam("admin") Admin admin, Model model) {

        User user = new User(admin);
        user.setPassword(PasswordGenerator.createRandomPassword());

        model.addAttribute("user", user);
        return "users/add-user-form";
    }

    @PostMapping("/save-user")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           Model model) {
        user.setUsername(user.getUsername().trim());
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
            String passwordHash = PasswordGenerator.createBCrypt(user.getPassword());
            user.setPassword(passwordHash);
        }

        adminService.saveUser(user);
        return "redirect:/users/user-list";
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

        user.setPassword(PasswordGenerator.createBCrypt(newPassword));
        adminService.saveUser(user);
        return "redirect:/users/user-list";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("user") User user) {
        adminService.deleteUser(user);
        return "redirect:/users/user-list";
    }
}
