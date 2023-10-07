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
import ru.questsfera.questreservation.service.AccountService;
import ru.questsfera.questreservation.service.AdminService;
import ru.questsfera.questreservation.service.UserService;
import ru.questsfera.questreservation.validator.Patterns;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final AccountService accountService;
    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public UserController(AccountService accountService, AdminService adminService, UserService userService) {
        this.accountService = accountService;
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/user-list")
    public String showUserList(Principal principal, Model model) {

        Admin admin = adminService.getAdminByName(principal.getName());
        List<User> users = userService.getUsersByAdmin(admin);

        model.addAttribute("users", users);
        return "users/user-list-page";
    }

    @PostMapping("/add-user")
    public String addUser(Principal principal, Model model) {

        Admin admin = adminService.getAdminByName(principal.getName());
        User user = new User(admin);
        user.setPassword(PasswordGenerator.createRandomPassword());

        model.addAttribute("user", user);
        return "users/add-update-user-form";
    }

    @PostMapping("/update-user")
    public String updateUser(@RequestParam("user") User user, Principal principal, Model model) {

        Admin admin = adminService.getAdminByName(principal.getName());
        userService.checkSecurityForUser(user, admin);

        model.addAttribute("user", user);
        return "users/add-update-user-form";
    }

    @PostMapping("/save-user")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           Principal principal,
                           Model model) {
        Admin admin = adminService.getAdminByName(principal.getName());
        user.setAdmin(admin);

        if (user.getId() != null) userService.checkSecurityForUser(user, admin);

        boolean existsUsername = accountService.existAccount(user.getEmail()) && user.getId() == null;

        if (bindingResult.hasErrors() || existsUsername) {

            if (existsUsername) {
                String errorMessage = "Username " + user.getEmail() + " уже существует";
                bindingResult.rejectValue("email", "errorCode", errorMessage);
            }

            model.addAttribute("user", user);
            return "users/add-update-user-form";
        }

        if (user.getId() == null) {
            String passwordHash = PasswordGenerator.createBCrypt(user.getPassword());
            user.setPassword(passwordHash);
        }

        user.setUsername(user.getEmail());
        userService.saveUser(user);
        return "redirect:/users/user-list";
    }

    @PostMapping("/update-password-user")
    public String updatePassword(@RequestParam("user") User user, Principal principal, Model model) {

        Admin admin = adminService.getAdminByName(principal.getName());
        userService.checkSecurityForUser(user, admin);

        model.addAttribute("user", user);
        model.addAttribute("new_password", "");
        model.addAttribute("error_password", false);
        return "users/update-password-form";
    }

    @PostMapping("/save-new-password")
    public String saveNewPassword(@RequestParam("user") User user, Model model,
                                  @RequestParam("new_password") String newPassword,
                                  Principal principal) {

        Admin admin = adminService.getAdminByName(principal.getName());
        userService.checkSecurityForUser(user, admin);

        if (!newPassword.matches(Patterns.PASSWORD)) {
            model.addAttribute("user", user);
            model.addAttribute("new_password", newPassword);
            model.addAttribute("error_password", true);
            return "users/update-password-form";
        }

        user.setPassword(PasswordGenerator.createBCrypt(newPassword));
        userService.saveUser(user);
        return "redirect:/users/user-list";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("user") User user, Principal principal) {
        Admin admin = adminService.getAdminByName(principal.getName());
        userService.deleteUser(user, admin);
        return "redirect:/users/user-list";
    }
}
