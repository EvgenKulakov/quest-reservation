package ru.questsfera.questreservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.questsfera.questreservation.model.dto.ReservationForm;
import ru.questsfera.questreservation.model.dto.Slot;
import ru.questsfera.questreservation.model.dto.SlotListPage;
import ru.questsfera.questreservation.model.session.SlotListPageSession;
import ru.questsfera.questreservation.service.reservation.ReservationGetOperator;
import ru.questsfera.questreservation.service.reservation.ReservationSaveOperator;
import ru.questsfera.questreservation.service.reservation.ReservationService;
import ru.questsfera.questreservation.validator.ValidType;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationSaveOperator reservationSaveOperator;
    private final ReservationGetOperator reservationGetOperator;
    private final SlotListPageSession slotListPageSession;

    @GetMapping("/slot-list")
    public String showSlotList(@RequestParam(value = "date", required = false) LocalDate date,
                               Principal principal, Model model) {

        if (date == null) date = LocalDate.now();
        SlotListPage slotListPage = reservationGetOperator.getQuestsAndSlotsByDate(date, principal);
        slotListPageSession.setSlotListPage(slotListPage);

        model.addAttribute("res_form", new ReservationForm());
        model.addAttribute("quest_names_and_slots", slotListPage.getQuestNamesAndSlots());
        model.addAttribute("use_statuses" , slotListPage.getUseStatuses());
        model.addAttribute("date", date);

        return "reservations/slot-list";
    }

    @PostMapping("/save-reservation")
    public String saveReservation(@Validated(ValidType.SaveReserve.class)
                                  @ModelAttribute("res_form") ReservationForm resForm,
                                  BindingResult bindingResult,
                                  @RequestParam("slot") Integer slotId,
                                  @RequestParam("date") LocalDate date,
                                  Principal principal,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        SlotListPage slotListPage = slotListPageSession.getSlotListPage();
        Slot slot = slotListPage.getSlotById(slotId).orElseThrow(SecurityException::new);

        if (bindingResult.hasErrors()) {
            return errorSlotListRendering(date, principal, slot, resForm, model);
        }

        reservationSaveOperator.saveUsingResFormAndSlot(resForm, slot);

        redirectAttributes.addAttribute("date", date);
        return "redirect:/reservations/slot-list";
    }

    @PostMapping("/block-slot")
    public String blockSlot(@Validated(ValidType.BlockSlot.class)
                            @ModelAttribute("res_form") ReservationForm resForm,
                            BindingResult bindingResult,
                            @RequestParam("slot") Integer slotId,
                            @RequestParam("date") LocalDate date,
                            Principal principal,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        SlotListPage slotListPage = slotListPageSession.getSlotListPage();
        Slot slot = slotListPage.getSlotById(slotId).orElseThrow(SecurityException::new);

        if (bindingResult.hasErrors()) {
            return errorSlotListRendering(date, principal, slot, resForm, model);
        }

        reservationSaveOperator.saveBlockReservationUsingSlot(slot);

        redirectAttributes.addAttribute("date", date);
        return "redirect:/reservations/slot-list";
    }

    @PostMapping("/unBlock")
    public String deleteBlockReserve(@RequestParam("slot") Integer sloId, RedirectAttributes redirectAttributes) {
        //TODO: редактирование вместо удаления
        SlotListPage slotListPage = slotListPageSession.getSlotListPage();
        Slot slot = slotListPage.getSlotById(sloId).orElseThrow(SecurityException::new);
        reservationService.deleteBlockedReservation(slot.getReservationId());
        redirectAttributes.addAttribute("date", slot.getDate());
        return "redirect:/reservations/slot-list";
    }

    private String errorSlotListRendering(LocalDate date, Principal principal,
                                          Slot errorSlot, ReservationForm resForm, Model model) {

        SlotListPage slotListPage = reservationGetOperator.getQuestsAndSlotsByDate(date, principal);
        Slot errorSlotFromSLotListPage = slotListPage
                .getSLotByDataAnotherSlot(errorSlot)
                .orElseThrow(SecurityException::new);
        slotListPageSession.setSlotListPage(slotListPage);

        model.addAttribute("res_form", resForm);
        model.addAttribute("quest_names_and_slots", slotListPage.getQuestNamesAndSlots());
        model.addAttribute("use_statuses" , slotListPage.getUseStatuses());
        model.addAttribute("date", date);
        model.addAttribute("error_slot", errorSlotFromSLotListPage);
        model.addAttribute("change_status", resForm.getStatus());
        model.addAttribute("change_count_persons", resForm.getCountPersons());

        return "reservations/slot-list";
    }
}