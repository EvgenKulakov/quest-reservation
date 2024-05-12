package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.dto.QuestForm;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.dto.SlotListTypeBuilder;
import ru.questsfera.questreservation.dto.TimePrice;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Status;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.processor.SlotListMaker;
import ru.questsfera.questreservation.service.AccountService;
import ru.questsfera.questreservation.service.QuestService;
import ru.questsfera.questreservation.validator.SlotListValidator;

import java.security.Principal;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quests")
public class QuestController {

    @Autowired
    private QuestService questService;
    @Autowired
    private AccountService accountService;


    @GetMapping("/quests-list")
    public String showQuestList(Principal principal, Model model) {

        Account account = accountService.getAccountByLogin(principal.getName());
        Set<Quest> quests = account.getQuests();//TODO: account.getQuests() ?

        model.addAttribute("quests", quests);
        return "quests/quests-list";
    }

    @PostMapping("/add-quest")
    public String addQuest(Principal principal, Model model) {

        Account account = accountService.getAccountByLogin(principal.getName());
        List<Account> allAccounts = accountService.getAccountsByCompany(account.getCompany());

        QuestForm questForm = new QuestForm();
        questForm.setStatuses(Status.getDefaultStatuses());
        questForm.setAutoBlock(LocalTime.MIN);
        questForm.setTypeBuilder(SlotListTypeBuilder.EQUAL_DAYS);
        questForm.setAccounts(allAccounts.stream().filter(acc -> acc.getRole() != Account.Role.ROLE_USER).toList());

        SlotListMaker.addDefaultValues(questForm.getSlotList());
        String slotListJSON = SlotListMapper.createJSON(questForm.getSlotList());

        model.addAttribute("quest_form", questForm);
        model.addAttribute("type_builders", SlotListTypeBuilder.values());
        model.addAttribute("slotlist_json", slotListJSON);
        model.addAttribute("all_accounts", allAccounts);
        model.addAttribute("user_statuses", Status.getUserStatuses());

        return "quests/add-quest";
    }

    @PostMapping("/save-quest")
    public String saveQuest(@Valid @ModelAttribute("quest_form") QuestForm questForm,
                            BindingResult binding,
                            Principal principal,
                            Model model) {

        Account account = accountService.getAccountByLogin(principal.getName());

//        userService.checkSecurityForUsers(questForm.getUsers(), admin);

        boolean existQuestName = questService.existQuestNameByCompany(questForm.getQuestName(), account.getCompany());
        String globalErrorMessage = SlotListValidator.checkByType(questForm.getSlotList(), questForm.getTypeBuilder());

        if (binding.hasErrors() || questForm.getMinPersons() > questForm.getMaxPersons()
                || existQuestName || !globalErrorMessage.isEmpty()) {

            if (questForm.getMinPersons() != null
                    && questForm.getMaxPersons() != null
                    && questForm.getMinPersons() > questForm.getMaxPersons()) {
                binding.rejectValue("errorCountPersons", "errorCode",
                        "*Значение \"От\" не может быть больше значения \"До\"");
            }

            if (existQuestName) {
                binding.rejectValue("questName", "errorCode",
                        String.format("У вас уже есть квест с названием \"%s\"", questForm.getQuestName()));
            }

            if (!globalErrorMessage.isEmpty()) {
                questForm.setOnlySecondPageError(!binding.hasFieldErrors());
                binding.addError(new ObjectError("global", globalErrorMessage));
            }

            String slotListJSON = SlotListMapper.createJSON(questForm.getSlotList());

            model.addAttribute("quest_form", questForm);
            model.addAttribute("type_builders", SlotListTypeBuilder.values());
            model.addAttribute("slotlist_json", slotListJSON);
            model.addAttribute("user_statuses", Status.getUserStatuses());
            model.addAttribute("all_accounts", accountService.getAccountsByCompany(account.getCompany()));

            return "quests/add-quest";
        }

        SlotListMaker.makeByType(questForm.getSlotList(), questForm.getTypeBuilder());
        Quest quest = new Quest(questForm, account.getCompany());
        questService.saveQuest(quest);

        for (Account acc : quest.getAccounts()) {
            acc.getQuests().add(quest);
            accountService.saveAccount(acc);
        }

        return "redirect:/quests/quests-list";
    }

    @PostMapping("/quest-info")
    public String showQuest(@RequestParam("quest") Quest quest,
                            Principal principal, Model model) {

//        Account account = accountService.getAccountByLogin(principal.getName());
//        questService.checkSecurityForQuest(quest, admin);

        Set<Account> accounts = new TreeSet<>((u1, u2) -> u1.getEmailLogin().compareToIgnoreCase(u2.getEmailLogin()));
        accounts.addAll(accountService.getAccountsByQuest(quest));

        SlotList slotList = SlotListMapper.createObject(quest.getSlotList());
        List<List<TimePrice>> allDays = slotList.getAllDays();

        model.addAttribute("quest", quest);
        model.addAttribute("accounts", accounts);
        model.addAttribute("all_slot_list", allDays);

        return "quests/quest-info";
    }

    @PostMapping("/delete-quest")
    public String deleteQuest(@RequestParam("quest") Quest quest,
                              Principal principal, Model model) {

        Account account = accountService.getAccountByLogin(principal.getName());

        if (questService.hasReservationsByQuest(quest)) {
//            questService.checkSecurityForQuest(quest, admin);
            model.addAttribute("quest", quest);
            return "quests/question-delete-quest";
        }

        questService.deleteQuest(quest, account.getCompany());
        return "redirect:/quests/quests-list";
    }

    @PostMapping("/delete-quest-final")
    public String deleteQuestFinal(@RequestParam("quest") Quest quest, Principal principal) {
        Account account = accountService.getAccountByLogin(principal.getName());
        questService.deleteQuest(quest, account.getCompany());
        return "redirect:/quests/quests-list";
    }
}
