package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.dto.ReservationForm;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.*;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.processor.Editor;
import ru.questsfera.questreservation.processor.SlotFactory;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.processor.StatusFactory;
import ru.questsfera.questreservation.service.AdminService;
import ru.questsfera.questreservation.validator.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@org.springframework.stereotype.Controller
public class ReservationController {

    private final AdminService adminService;

    private Map<String, List<Slot>> questsAndSlots = new LinkedHashMap<>();
    private Set<StatusType> useStatuses = new TreeSet<>();
    private LocalDate date;

    @Autowired
    public ReservationController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/slot-list")
    public String showSlotList(Model model) {
        /* test admin account */
        Admin admin = adminService.getAdminById(1);

        List<Quest> quests = adminService.getQuestsByAdmin(admin);
        LocalDate date = LocalDate.now();

        return slotListRendering(quests, date, model);
    }

    @GetMapping("/slot-list/")
    public String showSlotListWithDate(@RequestParam("date") LocalDate date, Model model) {
        /* test admin account */
        Admin admin = adminService.getAdminById(1);

        List<Quest> quests = adminService.getQuestsByAdmin(admin);

        return slotListRendering(quests, date, model);
    }

    public String slotListRendering(List<Quest> quests, LocalDate date, Model model) {

        questsAndSlots.clear();
        useStatuses.clear();

        for (Quest quest : quests) {
            if (quest.getSlotList() == null) continue;
            LinkedList<Reservation> reservations = adminService.getReservationsByDate(quest, date);
            SlotList slotList = SlotListMapper.createSlotListObject(quest.getSlotList());
            SlotFactory slotFactory = new SlotFactory(quest, date, slotList, reservations);
            List<Slot> slots = slotFactory.getActualSlots();
            StatusFactory.addUniqueStatusTypes(useStatuses, slots);
            questsAndSlots.put(quest.getQuestName(), slots);
        }

        model.addAttribute("quests_and_slots", questsAndSlots);
        model.addAttribute("use_statuses" , useStatuses);
        model.addAttribute("date", date);
        model.addAttribute("res_form", new ReservationForm());

        return "reservations/slot-list-page";
    }

    public String errorSlotListRendering(LocalDate date, String errorSlotJson, ReservationForm resForm, Model model) {

        model.addAttribute("quests_and_slots", questsAndSlots);
        model.addAttribute("use_statuses" , useStatuses);
        model.addAttribute("date", date);
        model.addAttribute("res_form", resForm);
        model.addAttribute("error_slot", errorSlotJson);
        model.addAttribute("change_status", resForm.getStatusType());
        model.addAttribute("change_count_persons", resForm.getCountPersons());

        return "reservations/slot-list-page";
    }

    @PostMapping("/save-reservation")
    public String saveReservation(@Validated(Validator.SaveReserve.class)
                                  @ModelAttribute("res_form") ReservationForm resForm,
                                  BindingResult bindingResult,
                                  @RequestParam("slot") String slotJSON,
                                  @RequestParam("date") LocalDate date,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            return errorSlotListRendering(date, slotJSON, resForm, model);
        }

        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = slot.getReservation();

        if (reservation == null) {
            reservation = new Reservation(resForm, slot);
            Client client = new Client(resForm, slot);
            reservation.addClient(client);
            reservation.setSourceReserve("default");
        } else {
            Editor.editReservation(reservation, resForm);
        }

        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default");
        adminService.saveReservation(reservation);

        return "redirect:/slot-list/?date=" + date;
    }

    @PostMapping("/block-slot")
    public String blockSlot(@Validated(Validator.BlockSlot.class)
                            @ModelAttribute("res_form") ReservationForm resForm,
                            BindingResult bindingResult,
                            @RequestParam("slot") String slotJSON,
                            @RequestParam("date") LocalDate date,
                            Model model) {

        if (bindingResult.hasErrors()) {
            return errorSlotListRendering(date, slotJSON, resForm, model);
        }

        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = Reservation.createBlockReservation(slot);

        reservation.setSourceReserve("default");
        reservation.setHistoryMessages("default");
        adminService.saveReservation(reservation);

        return "redirect:/slot-list/?date=" + date;
    }

    @PostMapping("/unBlock")
    public String deleteBlockReserve(@RequestParam("slot") String slotJSON) {
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = slot.getReservation();
        adminService.deleteBlockedReservation(reservation);
        return "redirect:/slot-list/?date=" + slot.getDate();
    }
}