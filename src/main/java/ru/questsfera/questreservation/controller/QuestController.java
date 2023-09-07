package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.dto.TimePrice;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Status;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.processor.SlotListFactory;
import ru.questsfera.questreservation.service.AdminService;

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

    @PostMapping("/add-quest-first")
    public String addQuest(Model model) {
        Quest quest = new Quest(admin);
        model.addAttribute("quest", quest);
        model.addAttribute("user_statuses", Status.getUserStatuses());
        return "add-quest-first-form";
    }

    @PostMapping("/add-slotlist-every-day")
    public String addQuestEveryDay(@ModelAttribute("quest") Quest quest, Model model) {
        adminService.saveQuest(admin, quest);
        model.addAttribute("quest" , quest);
        model.addAttribute("slot_list", new SlotList());
        return "add-slotlist-every-day-form";
    }

    @PostMapping("/save-quest")
    public String saveQuest(@ModelAttribute("slot_list") SlotList slotList,
                            @RequestParam("quest") Quest quest) {

        SlotListFactory.makeSlotListEveryDay(slotList);
        String jsonSlotList = SlotListMapper.createJSONSlotList(slotList);
        quest.setSlotList(jsonSlotList);

        adminService.saveQuest(admin, quest);

        return "redirect:/quest-list";
    }

    @PostMapping("/delete-quest")
    public String deleteQuest() {
        return "redirect:/quest-list";
    }
}
