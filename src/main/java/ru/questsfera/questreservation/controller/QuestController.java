package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.dto.TimePrice;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Status;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.processor.SlotListFactory;
import ru.questsfera.questreservation.service.AdminService;
import ru.questsfera.questreservation.validator.SlotListValidator;

import java.util.List;

@Controller
public class QuestController {

    private final AdminService adminService;
    private Admin admin;

    @Autowired
    public QuestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/quest-list")
    public String showQuestList(Model model) {
        /* admin test account */
        admin = adminService.getAdminById(1);

        List<Quest> quests = adminService.getQuestsByAdmin(admin);

        model.addAttribute("quests", quests);

        return "quest-list-page";
    }

    @GetMapping("/quest-info/{questId}")
    public String showQuest(@PathVariable("questId") int questId, Model model) {
        Quest quest = adminService.getQuestById(questId);
        SlotList slotList = SlotListMapper.createSlotListObject(quest.getSlotList());
        List<List<TimePrice>> allDays = slotList.getAllDays();

        model.addAttribute("quest", quest);
        model.addAttribute("all_slot_list", allDays);
        return "quest-info-page";
    }

    @PostMapping("/add-quest-first-page")
    public String addQuest(Model model) {
        Quest quest = new Quest(admin);
        model.addAttribute("quest", quest);
        model.addAttribute("user_statuses", Status.getUserStatuses());
        return "add-quest-first-form";
    }

    @PostMapping("/add-slotlist-every-day")
    public String addQuestEveryDay(@Valid @ModelAttribute("quest") Quest quest,
                                   BindingResult binding,
                                   Model model) {
        model.addAttribute("quest", quest);

        if (binding.hasErrors() || quest.getMinPersons() > quest.getMaxPersons()) {
            model.addAttribute("user_statuses", Status.getUserStatuses());

            if (quest.getMinPersons() != null
                    && quest.getMaxPersons() != null
                    && quest.getMinPersons() > quest.getMaxPersons()) {
                binding.addError(new ObjectError("global",
                        "*Значение \"От\" не может быть больше значения \"До\""));
            }
            return "add-quest-first-form";
        }

        adminService.saveQuest(admin, quest);
        model.addAttribute("slot_list", new SlotList());
        return "add-slotlist-every-day-form";
    }

    @PostMapping("/save-quest")
    public String saveQuest(@Valid @ModelAttribute("slot_list") SlotList slotList,
                            BindingResult binding,
                            @RequestParam("quest") Quest quest,
                            Model model) {

        String errorMessage = SlotListValidator.checkOneDay(slotList.getMonday());
        if (!errorMessage.isEmpty()) {
            binding.addError(new ObjectError("global", errorMessage));
            model.addAttribute("quest" , quest);
            model.addAttribute("slot_list", slotList);
            return "add-slotlist-every-day-form";
        }

        SlotListFactory.makeSlotListEveryDay(slotList);
        String jsonSlotList = SlotListMapper.createJSONSlotList(slotList);
        quest.setSlotList(jsonSlotList);

        adminService.saveQuest(admin, quest);

        return "redirect:/quest-list";
    }

    @PostMapping("/delete-quest")
    public String deleteQuest(@RequestParam("quest") Quest quest, Model model) {
        if (adminService.hasReservations(quest)) {
            model.addAttribute("quest", quest);
            return "question-delete-quest";

        }
        adminService.deleteQuest(admin, quest);
        return "redirect:/quest-list";
    }

    @PostMapping("/delete-quest-final")
    public String deleteQuestFinal(@RequestParam("quest") Quest quest) {
        adminService.deleteQuest(admin, quest);
        return "redirect:/quest-list";
    }
}
