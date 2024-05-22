package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.cache.object.AccountCache;
import ru.questsfera.questreservation.cache.service.*;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.dto.*;
import ru.questsfera.questreservation.entity.*;
import ru.questsfera.questreservation.processor.Editor;
import ru.questsfera.questreservation.processor.ReservationFactory;
import ru.questsfera.questreservation.processor.SlotFactory;
import ru.questsfera.questreservation.service.*;
import ru.questsfera.questreservation.validator.ValidType;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@org.springframework.stereotype.Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private QuestService questService;
    @Autowired
    private AccountCacheService accountCacheService;
    @Autowired
    private ReservationCacheService reservationCacheService;
    @Autowired
    private ClientCacheService clientCacheService;


    @GetMapping("/slot-list")
    public String showSlotList(Principal principal, Model model) {

        fillSlotList(LocalDate.now(), principal, model);
        model.addAttribute("res_form", new ReservationForm());

        return "reservations/slot-list";
    }

    @GetMapping("/slot-list/")
    public String showSlotListWithDate(@RequestParam("date") LocalDate date, Principal principal, Model model) {

        fillSlotList(date, principal, model);
        model.addAttribute("res_form", new ReservationForm());

        return "reservations/slot-list";
    }

    private void fillSlotList(LocalDate date, Principal principal, Model model) {

        AccountCache accountCache = accountCacheService.findByEmailLogin(principal.getName());
        Set<Quest> quests = new TreeSet<>(questService.findAllByAccountId(accountCache.getId()));
        Map<String, List<Slot>> questsAndSlots = new LinkedHashMap<>();
        Set<StatusType> useStatuses = new TreeSet<>();

        for (Quest quest : quests) {
            //TODO: query in cache
            LinkedList<Reservation> reservations = reservationService.findByQuestIdAndDate(quest.getId(), date);
            SlotList slotList = SlotListMapper.createObject(quest.getSlotList());
            SlotFactory slotFactory = new SlotFactory(quest, date, slotList, reservations);
            List<Slot> slots = slotFactory.getActualSlots();
            useStatuses.addAll(slots.stream().map(Slot::getStatusType).toList());
            questsAndSlots.put(quest.getQuestName(), slots);
        }

        model.addAttribute("quests_and_slots", questsAndSlots);
        model.addAttribute("use_statuses" , useStatuses);
        model.addAttribute("date", date);
    }

    private String errorSlotListRendering(LocalDate date, Principal principal,
                                          String errorSlotJson, ReservationForm resForm, Model model) {

        fillSlotList(date, principal, model);

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
            return errorSlotListRendering(date, principal, slotJSON, resForm, model);
        }

        AccountCache accountCache = accountCacheService.findByEmailLogin(principal.getName());
        Company company = companyService.findById(accountCache.getCompanyId());
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        Reservation reservation = null;

        if (slot.getReservation() == null) {
            reservation = ReservationFactory.createReservation(resForm, slot);
            reservationService.doubleCheck(reservation);
            Client client = new Client(resForm, company);
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
                            Principal principal,
                            Model model) {

        if (bindingResult.hasErrors()) {
            return errorSlotListRendering(date, principal, slotJSON, resForm, model);
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