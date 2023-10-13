package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.dto.*;
import ru.questsfera.questreservation.entity.*;
import ru.questsfera.questreservation.processor.Editor;
import ru.questsfera.questreservation.processor.ReservationFactory;
import ru.questsfera.questreservation.processor.SlotFactory;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.service.AccountService;
import ru.questsfera.questreservation.service.ReservationService;
import ru.questsfera.questreservation.validator.Validator;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@org.springframework.stereotype.Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final AccountService accountService;
    private final ReservationService reservationService;

    private Map<String, List<Slot>> questsAndSlots = new LinkedHashMap<>();
    private Set<StatusType> useStatuses = new TreeSet<>();

    @Autowired
    public ReservationController(AccountService accountService, ReservationService reservationService) {
        this.accountService = accountService;
        this.reservationService = reservationService;
    }

    @GetMapping("/slot-list")
    public String showSlotList(Principal principal, Model model) {
        Account account = accountService.getAccountByName(principal.getName());

        Set<Quest> quests = account.getQuests();
        LocalDate date = LocalDate.now();

        return slotListRendering(quests, date, model);
    }

    @GetMapping("/slot-list/")
    public String showSlotListWithDate(@RequestParam("date") LocalDate date,
                                       Principal principal, Model model) {

        Account account = accountService.getAccountByName(principal.getName());
        Set<Quest> quests = account.getQuests();

        return slotListRendering(quests, date, model);
    }

    public String slotListRendering(Set<Quest> quests, LocalDate date, Model model) {

        questsAndSlots.clear();
        useStatuses.clear();

        for (Quest quest : quests) {
            if (quest.getSlotList() == null) continue;
            LinkedList<Reservation> reservations = reservationService.getReservationsByDate(quest, date);
            SlotList slotList = SlotListMapper.createSlotListObject(quest.getSlotList());
            SlotFactory slotFactory = new SlotFactory(quest, date, slotList, reservations);
            List<Slot> slots = slotFactory.getActualSlots();
            useStatuses.addAll(slots.stream().map(Slot::getStatusType).toList());
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
                                  Principal principal,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            return errorSlotListRendering(date, slotJSON, resForm, model);
        }

        Account account = accountService.getAccountByName(principal.getName());
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = null;

        if (slot.getReservation() == null) {
            reservation = ReservationFactory.createReservation(resForm, slot, account.getAdmin());
            reservationService.doubleCheck(reservation);
            Client client = new Client(resForm, account.getAdmin());
            reservation.addClient(client);
            reservation.setSourceReserve("default");
        } else {
            reservation = reservationService.getReserveById(slot.getReservation().getId());
            Editor.editReservation(reservation, resForm);
        }

        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default");
        reservationService.saveReservation(reservation, account);

        return "redirect:/reservations/slot-list/?date=" + date;
    }

    @PostMapping("/block-slot")
    public String blockSlot(@Validated(Validator.BlockSlot.class)
                            @ModelAttribute("res_form") ReservationForm resForm,
                            BindingResult bindingResult,
                            @RequestParam("slot") String slotJSON,
                            @RequestParam("date") LocalDate date,
                            Principal principal,
                            Model model) {

        if (bindingResult.hasErrors()) {
            return errorSlotListRendering(date, slotJSON, resForm, model);
        }

        Account account = accountService.getAccountByName(principal.getName());
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = ReservationFactory.createBlockReservation(slot, account.getAdmin());
        reservationService.doubleCheck(reservation);

        reservation.setSourceReserve("default");
        reservation.setHistoryMessages("default");
        reservationService.saveReservation(reservation, account);

        return "redirect:/reservations/slot-list/?date=" + date;
    }

    @PostMapping("/unBlock")
    public String deleteBlockReserve(@RequestParam("slot") String slotJSON, Principal principal) {

        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = reservationService.getReserveById(slot.getReservation().getId());
        Account account = accountService.getAccountByName(principal.getName());

        reservationService.deleteBlockedReservation(reservation, account);
        return "redirect:/reservations/slot-list/?date=" + slot.getDate();
    }
}