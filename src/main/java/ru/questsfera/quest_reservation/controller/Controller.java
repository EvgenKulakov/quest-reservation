package ru.questsfera.quest_reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import ru.questsfera.quest_reservation.entity.Admin;
import ru.questsfera.quest_reservation.entity.Quest;
import ru.questsfera.quest_reservation.entity.Status;
import ru.questsfera.quest_reservation.service.AdminService;
import ru.questsfera.quest_reservation.service.ModeratorService;

import java.util.Set;


@org.springframework.stereotype.Controller
public class Controller {

    private final AdminService adminService;
    private final ModeratorService moderatorService;

    @Autowired
    public Controller(AdminService adminService, ModeratorService moderatorService) {
        this.adminService = adminService;
        this.moderatorService = moderatorService;
    }

//    @GetMapping("/")
//    public String testPage(Model model) {
//        List<Admin> allAdmins = adminService.getAllAdmins();
//        model.addAttribute("admins", allAdmins);
//        return "all-admins";
//    }
//
//    @GetMapping("/add-admin")
//    public String addAdmin() {
//        Admin admin = new Admin("567@mai.ru", "789789");
//        adminService.saveAdmin(admin);
//        return "redirect:/";
//    }

    @GetMapping("/tt")
    public String testJPA() {

        Admin admin4 = adminService.getAdminById(4);

        System.out.println(admin4.getUsername());

        return "test1";
    }

}
