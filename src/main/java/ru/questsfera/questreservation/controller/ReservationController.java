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
import ru.questsfera.questreservation.service.QuestService;
import ru.questsfera.questreservation.service.ReservationService;
import ru.questsfera.questreservation.validator.ValidType;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@org.springframework.stereotype.Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private QuestService questService;

    private Map<String, List<Slot>> questsAndSlots = new LinkedHashMap<>();
    private Set<StatusType> useStatuses = new TreeSet<>();

    @GetMapping("/slot-list")
    public String showSlotList(Principal principal, Model model) {
        Account account = accountService.getAccountByLogin(principal.getName());

        Set<Quest> quests = new TreeSet<>(questService.getQuestsByAccount(account));
        LocalDate date = LocalDate.now();

        return slotListRendering(quests, date, model);
    }

    @GetMapping("/slot-list/")
    public String showSlotListWithDate(@RequestParam("date") LocalDate date,
                                       Principal principal, Model model) {

        Account account = accountService.getAccountByLogin(principal.getName());
        Set<Quest> quests = new TreeSet<>(questService.getQuestsByAccount(account));

        return slotListRendering(quests, date, model);
    }

    public String slotListRendering(Set<Quest> quests, LocalDate date, Model model) {

        questsAndSlots.clear();
        useStatuses.clear();

        for (Quest quest : quests) {
            LinkedList<Reservation> reservations = reservationService.getReservationsByDate(quest, date);
            SlotList slotList = SlotListMapper.createObject(quest.getSlotList());
            SlotFactory slotFactory = new SlotFactory(quest, date, slotList, reservations);
            List<Slot> slots = slotFactory.getActualSlots();
            useStatuses.addAll(slots.stream().map(Slot::getStatusType).toList());
            questsAndSlots.put(quest.getQuestName(), slots);
        }

        model.addAttribute("quests_and_slots", questsAndSlots);
        model.addAttribute("use_statuses" , useStatuses);
        model.addAttribute("date", date);
        model.addAttribute("res_form", new ReservationForm());

        return "reservations/slot-list";
    }

    public String errorSlotListRendering(LocalDate date, String errorSlotJson, ReservationForm resForm, Model model) {

        model.addAttribute("quests_and_slots", questsAndSlots);
        model.addAttribute("use_statuses" , useStatuses);
        model.addAttribute("date", date);
        model.addAttribute("res_form", resForm);
        model.addAttribute("error_slot", errorSlotJson);
        model.addAttribute("change_status", resForm.getStatusType());
        model.addAttribute("change_count_persons", resForm.getCountPersons());

        return "reservations/slot-list";
    }

    @PostMapping("/save-reservation")
    public String saveReservation(@Validated(ValidType.SaveReserve.class)
                                  @ModelAttribute("res_form") ReservationForm resForm,
                                  BindingResult bindingResult,
                                  @RequestParam("slot") String slotJSON,
                                  @RequestParam("date") LocalDate date,
                                  Principal principal,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            return errorSlotListRendering(date, slotJSON, resForm, model);
        }

        Account account = accountService.getAccountByLogin(principal.getName());
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = null;

        if (slot.getReservation() == null) {
            reservation = ReservationFactory.createReservation(resForm, slot);
            reservationService.doubleCheck(reservation);
            Client client = new Client(resForm, account.getCompany());
            reservation.setClient(client);
            reservation.setSourceReserve("default"); //TODO: source reserve
        } else {
            reservation = reservationService.getReserveById(slot.getReservation().getId());
            Editor.editReservation(reservation, resForm);
        }

        reservation.setTimeLastChange(LocalDateTime.now());
        reservation.setHistoryMessages("default"); //TODO: history message
        reservationService.saveReservation(reservation);

        return "redirect:/reservations/slot-list/?date=" + date;
    }

    @PostMapping("/block-slot")
    public String blockSlot(@Validated(ValidType.BlockSlot.class)
                            @ModelAttribute("res_form") ReservationForm resForm,
                            BindingResult bindingResult,
                            @RequestParam("slot") String slotJSON,
                            @RequestParam("date") LocalDate date,
                            Model model) {

        if (bindingResult.hasErrors()) {
            return errorSlotListRendering(date, slotJSON, resForm, model);
        }

        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = ReservationFactory.createBlockReservation(slot);
        reservationService.doubleCheck(reservation);

        reservation.setSourceReserve("default"); //TODO: source reserve
        reservation.setHistoryMessages("default"); //TODO: history message
        reservationService.saveReservation(reservation);

        return "redirect:/reservations/slot-list/?date=" + date;
    }

    @PostMapping("/unBlock")
    public String deleteBlockReserve(@RequestParam("slot") String slotJSON) {

        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = reservationService.getReserveById(slot.getReservation().getId());

        reservationService.deleteBlockedReservation(reservation);
        return "redirect:/reservations/slot-list/?date=" + slot.getDate();
    }
}