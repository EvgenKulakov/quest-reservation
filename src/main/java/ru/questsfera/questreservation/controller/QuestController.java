package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.mapper.SlotListJsonMapper;
import ru.questsfera.questreservation.model.dto.*;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.processor.SlotListMaker;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.quest.QuestService;
import ru.questsfera.questreservation.service.reservation.ReservationService;
import ru.questsfera.questreservation.validator.SlotListValidator;

import java.security.Principal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/quests")
public class QuestController {

    private final QuestService questService;
    private final AccountService accountService;
    private final ReservationService reservationService;

    @GetMapping("/")
    public String showQuestList(Principal principal, Model model) {
        Set<Quest> quests = questService.findAllByAccount_login(principal.getName());
        model.addAttribute("quests", quests);
        return "quests/quests-list";
    }

    @PostMapping("/add-form")
    public String addQuest(Principal principal, Model model) {

        List<Account> allAccounts = accountService.findAllAccountsInCompanyByOwnAccountName(principal.getName());
        Account myAccount = allAccounts.stream()
                .filter(ac -> ac.getLogin().equals(principal.getName()))
                .findFirst()
                .orElseThrow();

        QuestForm questForm = new QuestForm();
        questForm.setStatuses(Status.MANDATORY_STATUSES);
        questForm.setAutoBlock(LocalTime.MIN);
        questForm.setTypeBuilder(SlotListTypeBuilder.EQUAL_DAYS);
        questForm.setAccounts(new ArrayList<>(List.of(myAccount)));

        SlotListMaker.addDefaultValues(questForm.getSlotList());
        String slotListJSON = SlotListJsonMapper.toJSON(questForm.getSlotList());

        model.addAttribute("questForm", questForm);
        model.addAttribute("typeBuilders", SlotListTypeBuilder.values());
        model.addAttribute("slotListJSON", slotListJSON);
        model.addAttribute("allAccounts", allAccounts);
        model.addAttribute("defaultStatuses", Status.DEFAULT_STATUSES);

        return "quests/add-quest-form";
    }

    @PostMapping("/save-quest")
    public String saveQuest(@Valid @ModelAttribute("questForm") QuestForm questForm,
                            BindingResult binding,
                            Principal principal,
                            Model model) {

        Account account = accountService.getAccountByLogin(principal.getName());

//        accountService.checkSecurityForAccounts(questForm.getAccounts(), account); // TODO security

        boolean existQuestName = questService.existQuestNameByCompany(questForm.getQuestName(), account.getCompanyId());
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

            String slotListJSON = SlotListJsonMapper.toJSON(questForm.getSlotList());

            model.addAttribute("questForm", questForm);
            model.addAttribute("typeBuilders", SlotListTypeBuilder.values());
            model.addAttribute("slotListJSON", slotListJSON);
            model.addAttribute("defaultStatuses", Status.DEFAULT_STATUSES);
            model.addAttribute("allAccounts", accountService.findAllAccountsInCompanyByOwnAccountName(principal.getName()));

            return "quests/add-quest-form";
        }

        SlotListMaker.makeByType(questForm.getSlotList(), questForm.getTypeBuilder());
        String slotListJson = SlotListJsonMapper.toJSON(questForm.getSlotList());
        Quest quest =  Quest.fromQuestFormSlotListCompanyId(questForm, slotListJson, account.getCompanyId());
        questService.saveQuest(quest);

        return "redirect:/quests/";
    }

    @PostMapping("/quest-info")
    public String showQuest(@RequestParam("quest") Quest quest, Model model) {

        List<Account> accounts = accountService.getAccountsByQuest(quest);

        SlotList slotList = SlotListJsonMapper.toObject(quest.getSlotList());
        List<List<TimePrice>> allDays = slotList.getAllDays();

        model.addAttribute("quest", quest);
        model.addAttribute("accounts", accounts);
        model.addAttribute("allSlotList", allDays);

        return "quests/quest-info";
    }

    @PostMapping("/delete")
    public String deleteQuest(@RequestParam("quest") Quest quest,
                              Principal principal, Model model) {

        if (reservationService.hasReservationsByQuest(quest)) {
            model.addAttribute("quest", quest);
            return "quests/question-delete-quest";
        }

        questService.deleteQuest(quest);
        return "redirect:/quests/";
    }

    @PostMapping("/delete-final")
    public String deleteQuestFinal(@RequestParam("quest") Quest quest, Principal principal) {
        questService.deleteQuest(quest);
        return "redirect:/quests/";
    }
}
