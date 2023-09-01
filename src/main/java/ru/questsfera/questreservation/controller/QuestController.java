package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.User;
import ru.questsfera.questreservation.processor.SlotListMapper;
import ru.questsfera.questreservation.service.AdminService;
import ru.questsfera.questreservation.service.ModeratorService;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class QuestController {

    private final AdminService adminService;
    private final ModeratorService moderatorService;
    private Admin admin;

    @Autowired
    public QuestController(AdminService adminService, ModeratorService moderatorService) {
        this.adminService = adminService;
        this.moderatorService = moderatorService;
    }


    @GetMapping("/quest-list")
    public String showQuestList(Model model) {
        admin = adminService.getAdminById(1);

        List<Quest> quests = adminService.getQuestsByAdmin(admin);

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
        Quest quest = new Quest(admin);
        SlotList slotList = new SlotList();
        model.addAttribute("quest", quest);
        model.addAttribute("slotList", slotList);
        return "add-quest-form";
    }

    @PostMapping("/save-quest")
    public String saveQuest(@ModelAttribute("quest") Quest quest, Model model,
                            @RequestParam("checkStatus") List<StatusType> statusTypes,
                            @RequestParam(value = "checkUser", required = false) Set<User> users,
                            @RequestParam("keysWeekday") List<LocalTime> keysWeekday,
                            @RequestParam("valuesWeekday") List<Integer> valuesWeekday,
                            @RequestParam("keysWeekend") List<LocalTime> keysWeekend,
                            @RequestParam("valuesWeekend") List<Integer> valuesWeekend) {
        return "redirect:/quest-list";
    }

    @PostMapping("/delete-quest")
    public String deleteQuest() {
        return "redirect:/quest-list";
    }
}