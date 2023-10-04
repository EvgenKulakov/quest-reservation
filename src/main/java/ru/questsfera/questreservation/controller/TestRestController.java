package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.service.AdminService;

import java.time.LocalTime;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class TestRestController {

    private final AdminService adminService;

    @Autowired
    public TestRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String showMainPage() {
        return "Главная страница";
    }

    @GetMapping("/slot/{id}")
    public Slot getSlot(@PathVariable("id") Integer id) {
        Reservation reservation = adminService.getReserveById(id);
        Slot slot = null;
        if (reservation != null) {
            slot = new Slot(reservation.getQuest(), reservation.getStatusType(), reservation,
                    reservation.getDateReserve(), reservation.getTimeReserve(), 3000, LocalTime.MIN);
        } else {
            slot = new Slot();
        }
        return slot;
    }

    @GetMapping("/quest/{id}")
    public Quest getQuest(@PathVariable("id") Integer id) {
        Quest quest = adminService.getQuestById(id);
        return quest;
    }

    @GetMapping("/reservation/{id}")
    public Reservation getReservation(@PathVariable("id") Integer id) {
        Reservation reservation = adminService.getReserveById(id);
        return reservation;
    }
}
