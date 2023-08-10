package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.processor.SlotFactory;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.processor.SlotListMapper;
import ru.questsfera.questreservation.service.AdminService;
import ru.questsfera.questreservation.service.ClientService;
import ru.questsfera.questreservation.service.ModeratorService;
import ru.questsfera.questreservation.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    private final AdminService adminService;
    private final ModeratorService moderatorService;
    private final UserService userService;
    private final ClientService clientService;
    private List<Slot> slots;
    private Admin admin;
    private Quest quest;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public Controller(AdminService adminService, ModeratorService moderatorService,
                      UserService userService, ClientService clientService) {
        this.adminService = adminService;
        this.moderatorService = moderatorService;
        this.userService = userService;
        this.clientService = clientService;
    }

    @GetMapping("/slot-list")
    public String showSlotList(Model model) {

        admin = adminService.getAdminById(1);
        quest = moderatorService.getQuestById(15);

        LocalDate date = LocalDate.now();
        String strDate = date.format(format);

        LinkedList<Reservation> reservations = adminService.getReservationsByDate(admin, quest, date);
        SlotList slotList = SlotListMapper.createSlotList(quest.getSlotList());
        SlotFactory slotFactory = new SlotFactory(quest, date, slotList, reservations);
        slots = slotFactory.getSlots();

        model.addAttribute("slots", slots);
        model.addAttribute("quest", quest);
        model.addAttribute("date", strDate);

        return "slot-list-page";
    }

    @GetMapping("/reservation/{slotId}")
    private String showReservation(@PathVariable("slotId") int slotId, Model model) {
        Slot slot = slots.get(slotId);

        Reservation reservation = slot.getReservation();
        if (reservation == null) {
            reservation = new Reservation();
            reservation.setQuest(slot.getQuest());
            reservation.addClient(new Client(admin));
            reservation.setDateReserve(slot.getDate());
            reservation.setTimeReserve(slot.getTime());
        }

        LocalDate date = slot.getDate();
        String dateFormat = date.format(format);

        model.addAttribute("reservation", reservation);
        model.addAttribute("slot", slot);
        model.addAttribute("date_format", dateFormat);

        return "reservation-form";
    }

    @PostMapping("/reservation/save-reservation")
    public String saveReservation(@ModelAttribute("reservation") Reservation reservation) {

        reservation.setSourceReserve("default");
        reservation.setHistoryMessages("default");

        System.out.println(reservation);

        adminService.saveReservation(admin, reservation);
        return "redirect:/slot-list";
    }

}