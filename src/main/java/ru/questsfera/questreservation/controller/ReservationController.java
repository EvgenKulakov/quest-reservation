package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
import ru.questsfera.questreservation.validator.SaveReserveValidator;
import ru.questsfera.questreservation.validator.BlockSlotValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@org.springframework.stereotype.Controller
public class ReservationController {

    private final AdminService adminService;
    private Map<String, List<Slot>> questsAndSlots = new LinkedHashMap<>();
    private Admin admin;

    @Autowired
    public ReservationController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/slot-list")
    public String showSlotList(Model model) {
        /* test admin account */
        admin = adminService.getAdminById(1);

        List<Quest> quests = adminService.getQuestsByAdmin(admin);
        LocalDate date = LocalDate.now();

        questsAndSlots.clear();

        Set<StatusType> useStatuses = new TreeSet<>();

        for (Quest quest : quests) {
            LinkedList<Reservation> reservations = adminService.getReservationsByDate(admin, quest, date);
            SlotList slotList = SlotListMapper.createSlotListObject(quest.getSlotList());
            SlotFactory slotFactory = new SlotFactory(quest, date, slotList, reservations);
            List<Slot> slots = slotFactory.getActualSlots();
            StatusFactory.addUniqueStatusTypes(useStatuses, slots);
            questsAndSlots.put(quest.getQuestName(), slots);
        }

        model.addAttribute("quests_and_slots", questsAndSlots);
        model.addAttribute("use_statuses" , useStatuses);
        model.addAttribute("date", date);

        return "slot-list-page";
    }

    @GetMapping("/reservation/{questName}/{slotId}")
    private String showReservation(@PathVariable("questName") String questName,
                                   @PathVariable("slotId") Integer slotId,
                                   Model model) {
        Slot slot = questsAndSlots.get(questName).get(slotId);
        Reservation reservation = slot.getReservation();

        ReservationForm resForm = reservation == null
                ? new ReservationForm()
                : new ReservationForm(reservation);

        model.addAttribute("res_form", resForm);
        model.addAttribute("slot", slot);
        model.addAttribute("slot_id", slotId);
        model.addAttribute("quest", slot.getQuest());

        if (resForm.getStatusType() != null && resForm.getStatusType().equals(StatusType.BLOCK)) {
            return "blocked-reservation-form";
        }

        return "reservation-form";
    }

    @PostMapping("/save-reservation")
    public String saveReservation(@Validated(SaveReserveValidator.class)
                                  @ModelAttribute("res_form") ReservationForm resForm,
                                  BindingResult reserveBinding,
                                  @RequestParam("slot_id") Integer slotId,
                                  @RequestParam("quest_name") String questName,
                                  Model model) {
        Slot slot = questsAndSlots.get(questName).get(slotId);
        Reservation reservation = slot.getReservation();

        if (reserveBinding.hasErrors()) {
            model.addAttribute("res_form", resForm);
            model.addAttribute("slot", slot);
            model.addAttribute("slot_id", slotId);
            model.addAttribute("quest", slot.getQuest());
            return "reservation-form";
        }

        if (reservation == null) {
            reservation = new Reservation(resForm, slot);
            Client client = new Client(resForm, admin);
            reservation.addClient(client);
            reservation.setSourceReserve("default");
        } else {
            Editor.editReservation(reservation, resForm);
        }

        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default");
        adminService.saveReservation(admin, reservation);

        return "redirect:/slot-list";
    }

    @PostMapping("/block-slot")
    public String blockSlot(@Validated(BlockSlotValidator.class)
                            @ModelAttribute("res_form") ReservationForm resForm,
                            BindingResult reserveBinding,
                            @RequestParam("slot_id") Integer slotId,
                            @RequestParam("quest_name") String questName,
                            Model model) {
        Slot slot = questsAndSlots.get(questName).get(slotId);

        if (reserveBinding.hasErrors()) {
            model.addAttribute("res_form", resForm);
            model.addAttribute("slot", slot);
            model.addAttribute("slot_id", slotId);
            model.addAttribute("quest", slot.getQuest());
            return "reservation-form";
        }

        Reservation reservation = Reservation.createBlockReservation(slot);
        reservation.setSourceReserve("default");
        reservation.setHistoryMessages("default");
        adminService.saveReservation(admin, reservation);

        return "redirect:/slot-list";
    }

    @PostMapping("/delete-blocked-reservation")
    public String deleteBlockReserve(@RequestParam("slot_id") Integer slotId,
                                     @RequestParam("quest_name") String questName) {
        Slot slot = questsAndSlots.get(questName).get(slotId);
        Reservation reservation = slot.getReservation();
        adminService.deleteBlockedReservation(admin, reservation);
        return "redirect:/slot-list";
    }
}