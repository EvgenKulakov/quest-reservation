package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.questsfera.questreservation.converter.SlotMapper;
import ru.questsfera.questreservation.dto.*;
import ru.questsfera.questreservation.service.reservation.ReservationGetOperator;
import ru.questsfera.questreservation.service.reservation.ReservationSaveOperator;
import ru.questsfera.questreservation.service.reservation.ReservationService;
import ru.questsfera.questreservation.validator.ValidType;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationSaveOperator reservationSaveOperator;
    @Autowired
    private ReservationGetOperator reservationGetOperator;


    @GetMapping("/slot-list")
    public String showSlotList(@RequestParam(value = "date", required = false) LocalDate date,
                               Principal principal, Model model) {

        if (date == null) date = LocalDate.now();
        Map<String, List<Slot>> questsAndSlots = reservationGetOperator.getQuestsAndSlots(date, principal);
        Set<StatusType> useStatuses = getUniqueStatusTypes(questsAndSlots.values());

        model.addAttribute("res_form", new ReservationForm());
        model.addAttribute("quests_and_slots", questsAndSlots);
        model.addAttribute("use_statuses" , useStatuses);
        model.addAttribute("date", date);

        return "reservations/slot-list";
    }

    @PostMapping("/save-reservation")
    public String saveReservation(@Validated(ValidType.SaveReserve.class)
                                  @ModelAttribute("res_form") ReservationForm resForm,
                                  BindingResult bindingResult,
                                  @RequestParam("slot") String slotJSON,
                                  @RequestParam("date") LocalDate date,
                                  Principal principal,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return errorSlotListRendering(date, principal, slotJSON, resForm, model);
        }

        reservationSaveOperator.saveReservation(resForm, slotJSON, principal);

        redirectAttributes.addAttribute("date", date);
        return "redirect:/reservations/slot-list";
    }

    @PostMapping("/block-slot")
    public String blockSlot(@Validated(ValidType.BlockSlot.class)
                            @ModelAttribute("res_form") ReservationForm resForm,
                            BindingResult bindingResult,
                            @RequestParam("slot") String slotJSON,
                            @RequestParam("date") LocalDate date,
                            Principal principal,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return errorSlotListRendering(date, principal, slotJSON, resForm, model);
        }

        reservationSaveOperator.saveBlockReservation(slotJSON);

        redirectAttributes.addAttribute("date", date);
        return "redirect:/reservations/slot-list";
    }

    @PostMapping("/unBlock")
    public String deleteBlockReserve(@RequestParam("slot") String slotJSON, RedirectAttributes redirectAttributes) {
        //TODO: редактирование вместо удаления
        Slot slot = SlotMapper.createSlotObject(slotJSON);
        reservationService.deleteBlockedReservation(slot.getReservation().getId());
        redirectAttributes.addAttribute("date", slot.getDate());
        return "redirect:/reservations/slot-list";
    }

    private String errorSlotListRendering(LocalDate date, Principal principal,
                                          String errorSlotJson, ReservationForm resForm, Model model) {

        Map<String, List<Slot>> questsAndSlots = reservationGetOperator.getQuestsAndSlots(date, principal);
        Set<StatusType> useStatuses = getUniqueStatusTypes(questsAndSlots.values());

        model.addAttribute("res_form", resForm);
        model.addAttribute("quests_and_slots", questsAndSlots);
        model.addAttribute("use_statuses" , useStatuses);
        model.addAttribute("date", date);
        model.addAttribute("error_slot", errorSlotJson);
        model.addAttribute("change_status", resForm.getStatusType());
        model.addAttribute("change_count_persons", resForm.getCountPersons());

        return "reservations/slot-list";
    }

    private TreeSet<StatusType> getUniqueStatusTypes(Collection<List<Slot>> slots) {
        return slots.stream()
                .flatMap(List::stream)
                .map(Slot::getStatusType)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}