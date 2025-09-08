package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.mapper.SlotListJsonMapper;
import ru.questsfera.questreservation.model.dto.*;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.processor.SlotListFactory;
import ru.questsfera.questreservation.security.AccountUserDetails;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.quest.QuestService;
import ru.questsfera.questreservation.service.reservation.ReservationService;
import ru.questsfera.questreservation.validator.SlotListValidator;

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
    private final SlotListJsonMapper slotListJsonMapper;
    private final SlotListFactory slotListFactory;
    private final SlotListValidator slotListValidator;

    @GetMapping("/")
    public String showQuestList(Authentication authentication, Model model) {
        AccountUserDetails principal = (AccountUserDetails) authentication.getPrincipal();
        Set<Quest> quests = questService.findAllByAccount_id(principal.getId());
        model.addAttribute("quests", quests);
        return "quests/quests-list";
    }

    @PostMapping("/add-form")
    public String addForm(Authentication authentication, Model model) {
        AccountUserDetails principal = (AccountUserDetails) authentication.getPrincipal();

        List<Account> allAccounts = accountService.findAllAccountsInCompanyByOwnAccountId(principal.getId());
        Account myAccount = allAccounts.stream()
                .filter(ac -> ac.getLogin().equals(principal.getUsername()))
                .findFirst()
                .orElseThrow();

        QuestForm questForm = new QuestForm();
        questForm.setStatuses(Status.MANDATORY_STATUSES);
        questForm.setAutoBlock(LocalTime.MIN);
        questForm.setTypeBuild(SlotListTypeBuild.EQUAL_DAYS);
        questForm.setAccounts(new ArrayList<>(List.of(myAccount)));
        questForm.setSlotList(slotListFactory.createDefaultValues());

        String slotListJSON = slotListJsonMapper.toJSON(questForm.getSlotList());

        model.addAttribute("questForm", questForm);
        model.addAttribute("typeBuilds", SlotListTypeBuild.values());
        model.addAttribute("slotListJSON", slotListJSON);
        model.addAttribute("allAccounts", allAccounts);
        model.addAttribute("defaultStatuses", Status.DEFAULT_STATUSES);

        return "quests/add-quest-form";
    }

    @PostMapping("/save-new-quest")
    @PreAuthorize("hasPermission(#questForm.accounts, 'LIST_ACCOUNTS', 'OWNER')")
    public String saveNewQuest(@Valid @ModelAttribute("questForm") QuestForm questForm,
                               BindingResult binding,
                               Authentication authentication,
                               Model model) {

        AccountUserDetails principal = (AccountUserDetails) authentication.getPrincipal();
        Account myAccount = accountService.findAccountById(principal.getId());

        boolean existQuestName = questService.existQuestNameByCompany(questForm.getQuestName(), myAccount.getCompanyId());
        String globalErrorMessage = slotListValidator.checkByType(questForm.getSlotList(), questForm.getTypeBuild());

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

            String slotListJSON = slotListJsonMapper.toJSON(questForm.getSlotList());
            List<Account> allAccountsInCompany = accountService.findAllAccountsInCompanyByOwnAccountId(principal.getId());

            model.addAttribute("questForm", questForm);
            model.addAttribute("typeBuilds", SlotListTypeBuild.values());
            model.addAttribute("slotListJSON", slotListJSON);
            model.addAttribute("defaultStatuses", Status.DEFAULT_STATUSES);
            model.addAttribute("allAccounts", allAccountsInCompany);

            return "quests/add-quest-form";
        }

        SlotList slotList = slotListFactory.makeByType(questForm.getSlotList(), questForm.getTypeBuild());
        String slotListJson = slotListJsonMapper.toJSON(slotList);

        Quest quest = Quest.fromQuestFormSlotListCompanyId(questForm, slotListJson, myAccount.getCompanyId());
        questService.saveNewQuest(quest, myAccount);

        return "redirect:/quests/";
    }

    @PostMapping("/quest-info")
    @PreAuthorize("hasPermission(#quest, 'OWNER_OR_ADMIN')")
    public String showQuest(@RequestParam("quest") Quest quest, Model model) {

        List<Account> accounts = accountService.getAccountsByQuestId(quest.getId());

        SlotList slotList = slotListJsonMapper.toObject(quest.getSlotList());
        List<List<TimePrice>> allDays = slotList.getAllDays();

        model.addAttribute("quest", quest);
        model.addAttribute("accounts", accounts);
        model.addAttribute("allSlotList", allDays);

        return "quests/quest-info";
    }

    @PostMapping("/delete")
    @PreAuthorize("hasPermission(#quest, 'OWNER')")
    public String deleteQuest(@RequestParam("quest") Quest quest, Model model) {

        if (reservationService.hasReservationsByQuest(quest)) {
            model.addAttribute("quest", quest);
            return "quests/question-delete-quest";
        }

        questService.deleteQuest(quest);
        return "redirect:/quests/";
    }

    @PostMapping("/delete-final")
    @PreAuthorize("hasPermission(#quest, 'OWNER')")
    public String deleteQuestFinal(@RequestParam("quest") Quest quest) {
        questService.deleteQuest(quest);
        return "redirect:/quests/";
    }
}
