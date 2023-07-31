package ru.questsfera.quest_reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import ru.questsfera.quest_reservation.entity.*;
import ru.questsfera.quest_reservation.service.AdminService;
import ru.questsfera.quest_reservation.service.ClientService;
import ru.questsfera.quest_reservation.service.ModeratorService;
import ru.questsfera.quest_reservation.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    private final AdminService adminService;
    private final ModeratorService moderatorService;
    private final UserService userService;
    private final ClientService clientService;

    @Autowired
    public Controller(AdminService adminService, ModeratorService moderatorService, UserService userService, ClientService clientService) {
        this.adminService = adminService;
        this.moderatorService = moderatorService;
        this.userService = userService;
        this.clientService = clientService;
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

//        Admin admin2 = adminService.getAdminById(2);
//        Admin admin1 = adminService.getAdminById(1);

//        User user20 = userService.getUserById(20);
//        User user = new User("Anna", "678", admin1);

//        Quest quest6 = moderatorService.getQuest(6);
//        Status status4 = moderatorService.getStatusById(4);
//        Client client = new Client(admin1, "Garik", "+79996105544");
//        Client client6 = moderatorService.getClientById(3);
//        BlackList blackList = new BlackList("+7984490099", "Не пришёл");
//        BlackList blackList1 = moderatorService.BlackListById(1);
//        LocalDate localDate = LocalDate.of(2023, 7, 15);
//        Reservation reservation = new Reservation(localDate, LocalTime.of(17, 30),
//                LocalDateTime.of(2023, 11, 6, 14, 12),
//                quest6, status4, "hey", 2, client6);
        Reservation reservation7 = moderatorService.getReserveById(7);
//        adminService.deleteBlackList(admin1, client3);
//        adminService.saveBlackList(admin1, client3, blackList1);
//        Reservation reservation = userService.getReserveById(user20, 9);
        reservation7.setCountPersons(8);


        clientService.saveReservation(reservation7);

        return "test1";
    }

}
