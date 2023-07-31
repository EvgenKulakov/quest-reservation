package ru.questsfera.quest_reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import ru.questsfera.quest_reservation.entity.*;
import ru.questsfera.quest_reservation.service.AdminService;
import ru.questsfera.quest_reservation.service.ModeratorService;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
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

        Admin admin2 = adminService.getAdminById(2);
//        Admin admin1 = adminService.getAdminById(1);


//        Quest quest6 = moderatorService.getQuest(6);
//        Status status4 = moderatorService.getStatusById(4);
//        Client client = new Client(admin1, "Nikita", "+79996665544");
//        Client client3 = moderatorService.getClientById(3);
//        BlackList blackList = new BlackList("+7984490099", "Не пришёл");
//        BlackList blackList1 = moderatorService.BlackListById(1);
//        Reservation reservation = new Reservation(new Date(1112229333L), new Time(1112922355L),
//                new Timestamp(12372344234L), quest6, status4, "src", 4);
        Reservation reservation1 = moderatorService.getReserveById(1);
//        adminService.deleteBlackList(admin1, client3);
//        adminService.saveBlackList(admin1, client3, blackList1);
//        Date date = Date.valueOf("1970-01-13");
//        LocalDate localDate = LocalDate.of(1970, 1, 13);

        System.out.println(reservation1.getQuest().getQuestName());

        return "test1";
    }

}
