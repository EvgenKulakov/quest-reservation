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
import ru.questsfera.questreservation.service.QuestService;
import ru.questsfera.questreservation.service.UserService;
import ru.questsfera.questreservation.validator.SlotListValidator;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Controller
@RequestMapping("/quests")
public class QuestController {

    private final AdminService adminService;
    private final QuestService questService;
    private final UserService userService;

    @Autowired
    public QuestController(AdminService adminService, QuestService questService, UserService userService) {
        this.adminService = adminService;
        this.questService = questService;
        this.userService = userService;
    }

    @GetMapping("/quests-list")
    public String showQuestList(Principal principal, Model model) {

        Admin admin = adminService.getAdminByName(principal.getName());
        List<Quest> quests = questService.getQuestsByAdmin(admin); // убрать EAGER у admin.getQuests()

        model.addAttribute("quests", quests);
        return "quests/quests-list";
    }

    @PostMapping("/add-quest")
    public String addQuest(Principal principal, Model model) {

        Admin admin = adminService.getAdminByName(principal.getName());
        Quest quest = new Quest(admin);
        List<User> allUsers = userService.getUsersByAdmin(admin);

        model.addAttribute("quest", quest);
        model.addAttribute("all_users", allUsers);
        model.addAttribute("user_statuses", Status.getUserStatuses());

        return "quests/add-quest";
    }

    @PostMapping("/add-slotlist")
    public String addQuestEveryDay(@Valid @ModelAttribute("quest") Quest quest,
                                   BindingResult binding,
                                   Principal principal,
                                   Model model) {

        Admin admin = adminService.getAdminByName(principal.getName());
        userService.checkSecurityForUsers(quest.getUsers(), admin);

        quest.setQuestName(quest.getQuestName().trim());
        model.addAttribute("quest", quest);

        boolean existQuestName = questService.existQuestNameByAdmin(quest.getQuestName(), admin);

        if (binding.hasErrors() || quest.getMinPersons() > quest.getMaxPersons() || existQuestName) {
            model.addAttribute("user_statuses", Status.getUserStatuses());
            model.addAttribute("all_users", userService.getUsersByAdmin(admin));

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
            return "quests/add-quest";
        }

        quest.setAdmin(admin);
        questService.saveQuest(quest);

        model.addAttribute("slot_list", new SlotList());
        return "quests/add-slotlist";
    }

    @PostMapping("/quest-info")
    public String showQuest(@RequestParam("quest") Quest quest,
                            Principal principal, Model model) {

        Admin admin = adminService.getAdminByName(principal.getName());
        questService.checkSecurityForQuest(quest, admin);

        Set<User> users = new TreeSet<>((u1, u2) -> u1.getUsername().compareToIgnoreCase(u2.getUsername()));
        users.addAll(quest.getUsers());

        List<List<TimePrice>> allDays = null;
        if (quest.getSlotList() != null) { // убрать проверку после добавления JS для сохранения
            SlotList slotList = SlotListMapper.createSlotListObject(quest.getSlotList());
            allDays = slotList.getAllDays();
        }

        model.addAttribute("quest", quest);
        model.addAttribute("users", users);
        model.addAttribute("all_slot_list", allDays);

        return "quests/quest-info";
    }

    @PostMapping("/save-quest")
    public String saveQuest(@Valid @ModelAttribute("slot_list") SlotList slotList,
                            BindingResult binding,
                            @RequestParam("quest") Quest quest,
                            Principal principal,
                            Model model) {

        Admin admin = adminService.getAdminByName(principal.getName());
        questService.checkSecurityForQuest(quest, admin);

        String errorMessage = SlotListValidator.checkOneDay(slotList.getMonday());
        if (!errorMessage.isEmpty()) {
            binding.addError(new ObjectError("global", errorMessage));
            model.addAttribute("quest" , quest);
            model.addAttribute("slot_list", slotList);
            return "quests/add-slotlist";
        }

        SlotListFactory.makeSlotListEveryDay(slotList);
        String jsonSlotList = SlotListMapper.createJSONSlotList(slotList);
        quest.setSlotList(jsonSlotList);

        questService.saveQuest(quest);

        return "redirect:/quests/quests-list";
    }

    @PostMapping("/delete-quest")
    public String deleteQuest(@RequestParam("quest") Quest quest,
                              Principal principal, Model model) {

        Admin admin = adminService.getAdminByName(principal.getName());

        if (questService.hasReservations(quest)) {
            questService.checkSecurityForQuest(quest, admin);
            model.addAttribute("quest", quest);
            return "quests/question-delete-quest";
        }

        questService.deleteQuest(quest, admin);
        return "redirect:/quests/quests-list";
    }

    @PostMapping("/delete-quest-final")
    public String deleteQuestFinal(@RequestParam("quest") Quest quest, Principal principal) {
        Admin admin = adminService.getAdminByName(principal.getName());
        questService.deleteQuest(quest, admin);
        return "redirect:/quests/quests-list";
    }
}
