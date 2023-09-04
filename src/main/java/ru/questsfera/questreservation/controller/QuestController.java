package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Status;
import ru.questsfera.questreservation.entity.User;
import ru.questsfera.questreservation.converters.SlotListMapper;
import ru.questsfera.questreservation.processor.SlotListFactory;
import ru.questsfera.questreservation.service.AdminService;
import ru.questsfera.questreservation.service.ModeratorService;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
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
        /* admin test account */
        admin = adminService.getAdminById(1);

        List<Quest> quests = adminService.getQuestsByAdmin(admin);

        model.addAttribute("quests", quests);

        return "quest-list-page";
    }

    @GetMapping("/quest-info/{questId}")
    public String showQuest(@PathVariable("questId") int questId, Model model) {
        Quest quest = moderatorService.getQuestById(questId);
        SlotList slotList = SlotListMapper.createSlotListObject(quest.getSlotList());
        List<LinkedHashMap<LocalTime, Integer>> allDays = slotList.getAllDays();

        model.addAttribute("quest", quest);
        model.addAttribute("all_slot_list", allDays);
        return "quest-info-page";
    }

    @GetMapping("/add-quest")
    public String addQuest(Model model) {
        Quest quest = new Quest(admin);
        model.addAttribute("quest", quest);
        return "add-quest-form";
    }

    @PostMapping("/save-quest")
    public String saveQuest(@ModelAttribute("quest") Quest quest,
                            @RequestParam("checkStatus") Set<StatusType> statusTypes,
                            @RequestParam(value = "checkUser", required = false) Set<User> users,
                            @RequestParam("timesWeekday") List<LocalTime> timesWeekday,
                            @RequestParam("pricesWeekday") List<Integer> pricesWeekday,
                            @RequestParam("timesWeekend") List<LocalTime> timesWeekend,
                            @RequestParam("pricesWeekend") List<Integer> pricesWeekend) {

        Set<Status> statuses = adminService.getStatusesByTypes(statusTypes);
        quest.setStatuses(statuses);

        if (users != null) quest.setUsers(users);

        SlotList slotList = SlotListFactory.create(timesWeekday, pricesWeekday, timesWeekend, pricesWeekend);
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
