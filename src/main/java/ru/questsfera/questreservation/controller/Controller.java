package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.*;
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        SlotList slotList = SlotListMapper.createSlotListObject(quest.getSlotList());
        SlotFactory slotFactory = new SlotFactory(quest, date, slotList, reservations);
        slots = slotFactory.getActualSlots();

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

        if (reservation.getStatus() != null && reservation.getStatus().getType().equals(StatusType.BLOCK)) {
            return "blocked-reservation-form";
        }

        return "reservation-form";
    }

    @PostMapping("/save-reservation")
    public String saveReservation(@ModelAttribute("reservation") Reservation reservation,
                                  @RequestParam(value = "blocked", required = false) boolean block) {
        if (block) {
            reservation.setStatus(moderatorService.getStatusById(12));
            reservation.getClient().clear();
        }
        reservation.setSourceReserve("default");
        reservation.setHistoryMessages("default");
        adminService.saveReservation(admin, reservation);

        return "redirect:/slot-list";
    }

    @PostMapping("/delete-blocked-reservation")
    public String deleteBlockReserve(@ModelAttribute("reservation") Reservation reservation) {
        adminService.deleteBlockedReservation(admin, reservation);
        return "redirect:/slot-list";
    }

    @GetMapping("/quest-list")
    public String showQuestList(Model model) {
        admin = adminService.getAdminById(1);

        Set<Quest> quests = adminService.getQuestsByAdmin(admin);

        model.addAttribute("quests", quests);

        return "quest-list-page";
    }

    @GetMapping("/quest-info/{questId}")
    public String showQuest(@PathVariable("questId") int questId, Model model) {
        Quest quest = moderatorService.getQuestById(questId);
        SlotList slotList = SlotListMapper.createSlotListObject(quest.getSlotList());
        List<LinkedHashMap<String, Integer>> allDays = slotList.getAllDays();

        model.addAttribute("quest", quest);
        model.addAttribute("all_slot_list", allDays);
        return "quest-info-page";
    }

    @GetMapping("/add-quest")
    public String addQuest(Model model) {
        Quest quest = new Quest();
        model.addAttribute("quest", quest);
        return "add-quest-form";
    }

    @PostMapping("/save-quest")
    public String saveQuest() {
        return null;
    }

    @GetMapping("/test-save-quest")
    public String testSaveQuest(@ModelAttribute("quest") Quest quest, Model model,
                                @RequestParam(value = "checkStatus", required = false) List<StatusType> statuses) {
        statuses.forEach(statusType -> System.out.println(statusType));
        model.addAttribute("quest", quest);
        return "test-save-quest-page";
    }

    @PostMapping("/delete-quest")
    public String deleteQuest() {
        return "redirect:/quest-list";
    }

}