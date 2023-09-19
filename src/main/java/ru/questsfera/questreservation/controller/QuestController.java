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
import ru.questsfera.questreservation.entity.User;
import ru.questsfera.questreservation.processor.SlotListFactory;
import ru.questsfera.questreservation.service.AdminService;
import ru.questsfera.questreservation.validator.SlotListValidator;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

        model.addAttribute("admin" , admin);
        model.addAttribute("quests", quests);

        return "quests/quest-list-page";
    }

    @PostMapping("/quest-info")
    public String showQuest(@RequestParam("quest") Quest quest, Model model) {
        Set<User> users = new TreeSet<>((u1, u2) -> u1.getUsername().compareToIgnoreCase(u2.getUsername()));
        users.addAll(quest.getUsers());

        List<List<TimePrice>> allDays = null;
        if (quest.getSlotList() != null) {
            SlotList slotList = SlotListMapper.createSlotListObject(quest.getSlotList());
            allDays = slotList.getAllDays();
        }

        model.addAttribute("quest", quest);
        model.addAttribute("users", users);
        model.addAttribute("all_slot_list", allDays);

        return "quests/quest-info-page";
    }

    @PostMapping("/add-quest-first-page")
    public String addQuest(@RequestParam("admin") Admin admin, Model model) {
        Quest quest = new Quest(admin);
        List<User> allUsers = adminService.getUsersByAdmin(admin);

        model.addAttribute("quest", quest);
        model.addAttribute("all_users", allUsers);
        model.addAttribute("user_statuses", Status.getUserStatuses());

        return "quests/add-quest-first-form";
    }

    @PostMapping("/add-slotlist-every-day")
    public String addQuestEveryDay(@Valid @ModelAttribute("quest") Quest quest,
                                   BindingResult binding,
                                   Model model) {
        quest.setQuestName(quest.getQuestName().trim());
        model.addAttribute("quest", quest);

        boolean existQuestName = adminService.existQuestName(quest) && quest.getId() == null;

        if (binding.hasErrors() || quest.getMinPersons() > quest.getMaxPersons() || existQuestName) {
            model.addAttribute("user_statuses", Status.getUserStatuses());
            model.addAttribute("all_users", adminService.getUsersByAdmin(admin));

            if (quest.getMinPersons() != null
                    && quest.getMaxPersons() != null
                    && quest.getMinPersons() > quest.getMaxPersons()) {
                binding.addError(new ObjectError("global",
                        "*Значение \"От\" не может быть больше значения \"До\""));
            }

            if (existQuestName) {
                binding.rejectValue("questName", "errorCode",
                        String.format("У вас уже есть квест с названием \"%s\"", quest.getQuestName()));
            }
            return "quests/add-quest-first-form";
        }

        adminService.saveQuest(admin, quest);
        model.addAttribute("slot_list", new SlotList());
        return "quests/add-slotlist-every-day-form";
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
            return "quests/add-slotlist-every-day-form";
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
            return "quests/question-delete-quest";

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
