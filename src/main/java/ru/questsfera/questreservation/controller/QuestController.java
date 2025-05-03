package ru.questsfera.questreservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.converter.QuestMapper;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.dto.*;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Status;
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
    private final QuestMapper questMapper;

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

        QuestFormDTO questFormDTO = new QuestFormDTO();
        questFormDTO.setStatuses(Status.getDefaultStatuses());
        questFormDTO.setAutoBlock(LocalTime.MIN);
        questFormDTO.setTypeBuilder(SlotListTypeBuilder.EQUAL_DAYS);
        questFormDTO.setAccounts(new ArrayList<>(List.of(myAccount)));

        SlotListMaker.addDefaultValues(questFormDTO.getSlotList());
        String slotListJSON = SlotListMapper.createJSON(questFormDTO.getSlotList());

        model.addAttribute("questForm", questFormDTO);
        model.addAttribute("typeBuilders", SlotListTypeBuilder.values());
        model.addAttribute("slotListJSON", slotListJSON);
        model.addAttribute("allAccounts", allAccounts);
        model.addAttribute("userStatuses", Status.getUserStatuses());

        return "quests/add-quest-form";
    }

    @PostMapping("/save-quest")
    public String saveQuest(@Valid @ModelAttribute("questForm") QuestFormDTO questFormDTO,
                            BindingResult binding,
                            Principal principal,
                            Model model) {

        Account account = accountService.getAccountByLogin(principal.getName());

//        accountService.checkSecurityForAccounts(questForm.getAccounts(), account); // TODO security

        boolean existQuestName = questService.existQuestNameByCompany(questFormDTO.getQuestName(), account.getCompanyId());
        String globalErrorMessage = SlotListValidator.checkByType(questFormDTO.getSlotList(), questFormDTO.getTypeBuilder());

        if (binding.hasErrors() || questFormDTO.getMinPersons() > questFormDTO.getMaxPersons()
                || existQuestName || !globalErrorMessage.isEmpty()) {

            if (questFormDTO.getMinPersons() != null
                    && questFormDTO.getMaxPersons() != null
                    && questFormDTO.getMinPersons() > questFormDTO.getMaxPersons()) {
                binding.rejectValue("errorCountPersons", "errorCode",
                        "*Значение \"От\" не может быть больше значения \"До\"");
            }

            if (existQuestName) {
                binding.rejectValue("questName", "errorCode",
                        String.format("У вас уже есть квест с названием \"%s\"", questFormDTO.getQuestName()));
            }

            if (!globalErrorMessage.isEmpty()) {
                questFormDTO.setOnlySecondPageError(!binding.hasFieldErrors());
                binding.addError(new ObjectError("global", globalErrorMessage));
            }

            String slotListJSON = SlotListMapper.createJSON(questFormDTO.getSlotList());

            model.addAttribute("questForm", questFormDTO);
            model.addAttribute("typeBuilders", SlotListTypeBuilder.values());
            model.addAttribute("slotListJSON", slotListJSON);
            model.addAttribute("userStatuses", Status.getUserStatuses());
            model.addAttribute("allAccounts", accountService.findAllAccountsInCompanyByOwnAccountName(principal.getName()));

            return "quests/add-quest-form";
        }

        SlotListMaker.makeByType(questFormDTO.getSlotList(), questFormDTO.getTypeBuilder());
        Quest quest =  Quest.fromQuestFormAndCompanyId(questFormDTO, account.getCompanyId());
        questService.saveQuest(quest);

        return "redirect:/quests/";
    }

    @PostMapping("/quest-info")
    public String showQuest(@RequestParam("quest") Quest quest, Model model) {

        List<Account> accounts = accountService.getAccountsByQuest(quest);

        SlotList slotList = SlotListMapper.createObject(quest.getSlotList());
        List<List<TimePrice>> allDays = slotList.getAllDays();
        QuestDTO questDTO = questMapper.toDto(quest);

        model.addAttribute("quest", questDTO);
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
