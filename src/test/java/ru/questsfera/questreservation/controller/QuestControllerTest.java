package ru.questsfera.questreservation.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.questsfera.questreservation.mapper.SlotListJsonMapper;
import ru.questsfera.questreservation.model.dto.QuestForm;
import ru.questsfera.questreservation.model.dto.SlotList;
import ru.questsfera.questreservation.model.dto.SlotListTypeBuild;
import ru.questsfera.questreservation.model.dto.Status;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.processor.SlotListFactory;
import ru.questsfera.questreservation.security.AccountUserDetails;
import ru.questsfera.questreservation.security.DomainPermissionEvaluator;
import ru.questsfera.questreservation.security.SecurityConfig;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.quest.QuestService;
import ru.questsfera.questreservation.service.reservation.ReservationService;
import ru.questsfera.questreservation.validator.SlotListValidator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestController.class)
@Import(value = {SecurityConfig.class, DomainPermissionEvaluator.class})
class QuestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean QuestService questService;
    @MockitoBean AccountService accountService;
    @MockitoBean ReservationService reservationService;
    @MockitoBean SlotListJsonMapper slotListJsonMapper;
    @MockitoBean SlotListFactory slotListFactory;
    @MockitoBean SlotListValidator slotListValidator;

    @Test
    void showQuestList() throws Exception {
        when(questService.findAllByAccount_id(anyInt())).thenReturn(Set.of(new Quest()));

        mockMvc.perform(get("/quests/")
                .with(user(getPrincipal()))
        ).andExpect(model().attributeExists("quests")
        ).andExpect(model().attribute("quests", hasSize(1))
        ).andExpect(view().name("quests/quests-list")
        ).andExpect(status().isOk());
    }

    @Test
    void addForm() throws Exception {
        QuestForm questForm = getEmptyQuestForm();

        when(accountService.findAllAccountsInCompanyByOwnAccountId(anyInt())).thenReturn(List.of(getAccount(), getMyAccount()));
        when(slotListFactory.createDefaultValues()).thenReturn(questForm.getSlotList());
        when(slotListJsonMapper.toJSON(ArgumentMatchers.any(SlotList.class))).thenReturn("");

        mockMvc.perform(post("/quests/add-form")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("questForm", "typeBuilds", "slotListJSON", "allAccounts", "defaultStatuses")
        ).andExpect(model().attribute("questForm", is(questForm))
        ).andExpect(model().attribute("typeBuilds", is(SlotListTypeBuild.values()))
        ).andExpect(model().attribute("slotListJSON", notNullValue())
        ).andExpect(model().attribute("allAccounts", hasSize(2))
        ).andExpect(model().attribute("defaultStatuses", hasSize(5))
        ).andExpect(view().name("quests/add-quest-form")
        ).andExpect(status().isOk());
    }

    @Test
    void saveNewQuest_success() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getMyAccount());
        when(questService.existQuestNameByCompany(anyString(), anyInt())).thenReturn(Boolean.FALSE);
        when(slotListValidator.checkByType(any(), any())).thenReturn("");
        when(slotListFactory.makeByType(any(), any())).thenReturn(new SlotList());
        when(slotListJsonMapper.toJSON(ArgumentMatchers.any(SlotList.class))).thenReturn("");

        mockMvc.perform(post("/quests/save-new-quest")
                .flashAttr("questForm", getQuestForm())
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(view().name("redirect:/quests/")
        ).andExpect(status().is3xxRedirection());

        verify(questService).saveNewQuest(ArgumentMatchers.any(Quest.class), ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveNewQuest_principalAdminRole_forbidden() throws Exception {
        mockMvc.perform(post("/quests/save-new-quest")
                .flashAttr("questForm", getQuestForm())
                .with(user(getPrincipalWithAdminRole()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(questService, never()).saveNewQuest(ArgumentMatchers.any(Quest.class), ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveNewQuest_strangersAccounts_forbidden() throws Exception {
        mockMvc.perform(post("/quests/save-new-quest")
                .flashAttr("questForm", getQuestFormWithAccountDiffCompany())
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(questService, never()).saveNewQuest(ArgumentMatchers.any(Quest.class), ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveNewQuest_questNameError_failure() throws Exception {
        QuestForm questForm = getQuestForm();
        questForm.setQuestName("");
        saveNewQuest_questFormHasErrors_failure(questForm);
    }

    @Test
    void saveNewQuest_minPersonsError_failure() throws Exception {
        QuestForm questForm = getQuestForm();
        questForm.setMinPersons(0);
        saveNewQuest_questFormHasErrors_failure(questForm);
    }

    @Test
    void saveNewQuest_maxPersonsError_failure() throws Exception {
        QuestForm questForm = getQuestForm();
        questForm.setMaxPersons(101);
        saveNewQuest_questFormHasErrors_failure(questForm);
    }

    @Test
    void saveNewQuest_autoBlockError_failure() throws Exception {
        QuestForm questForm = getQuestForm();
        questForm.setAutoBlock(null);
        saveNewQuest_questFormHasErrors_failure(questForm);
    }

    @Test
    void saveNewQuest_countPersonsError_failure() throws Exception {
        QuestForm questForm = getQuestForm();
        questForm.setMinPersons(50);
        questForm.setMaxPersons(10);
        saveNewQuest_questFormHasErrors_failure(questForm);
    }

    private void saveNewQuest_questFormHasErrors_failure(QuestForm questForm) throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getMyAccount());
        when(questService.existQuestNameByCompany(anyString(), anyInt())).thenReturn(Boolean.FALSE);
        when(slotListValidator.checkByType(any(), any())).thenReturn("");
        when(slotListJsonMapper.toJSON(ArgumentMatchers.any(SlotList.class))).thenReturn("");
        when(accountService.findAllAccountsInCompanyByOwnAccountId(anyInt())).thenReturn(List.of(getAccount(), getMyAccount()));

        mockMvc.perform(post("/quests/save-new-quest")
                .flashAttr("questForm", questForm)
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("questForm", "typeBuilds", "slotListJSON", "defaultStatuses", "allAccounts")
        ).andExpect(model().attribute("questForm", is(questForm))
        ).andExpect(model().attribute("typeBuilds", is(SlotListTypeBuild.values()))
        ).andExpect(model().attribute("slotListJSON", notNullValue())
        ).andExpect(model().attribute("defaultStatuses", hasSize(5))
        ).andExpect(model().attribute("allAccounts", hasSize(2))
        ).andExpect(view().name("quests/add-quest-form")
        ).andExpect(status().isOk());

        verify(slotListFactory, never()).makeByType(ArgumentMatchers.any(SlotList.class), ArgumentMatchers.any(SlotListTypeBuild.class));
        verify(questService, never()).saveNewQuest(ArgumentMatchers.any(Quest.class), ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveNewQuest_existQuestNameError_failure() throws Exception {
        QuestForm questForm = getQuestForm();

        when(accountService.findAccountById(anyInt())).thenReturn(getMyAccount());
        when(questService.existQuestNameByCompany(anyString(), anyInt())).thenReturn(Boolean.TRUE);
        when(slotListValidator.checkByType(any(), any())).thenReturn("");
        when(slotListJsonMapper.toJSON(ArgumentMatchers.any(SlotList.class))).thenReturn("");
        when(accountService.findAllAccountsInCompanyByOwnAccountId(anyInt())).thenReturn(List.of(getAccount(), getMyAccount()));

        mockMvc.perform(post("/quests/save-new-quest")
                .flashAttr("questForm", questForm)
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("questForm", "typeBuilds", "slotListJSON", "defaultStatuses", "allAccounts")
        ).andExpect(model().attribute("questForm", is(questForm))
        ).andExpect(model().attribute("typeBuilds", is(SlotListTypeBuild.values()))
        ).andExpect(model().attribute("slotListJSON", notNullValue())
        ).andExpect(model().attribute("defaultStatuses", hasSize(5))
        ).andExpect(model().attribute("allAccounts", hasSize(2))
        ).andExpect(view().name("quests/add-quest-form")
        ).andExpect(status().isOk());

        verify(slotListFactory, never()).makeByType(ArgumentMatchers.any(SlotList.class), ArgumentMatchers.any(SlotListTypeBuild.class));
        verify(questService, never()).saveNewQuest(ArgumentMatchers.any(Quest.class), ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveNewQuest_globalErrorMessage_failure() throws Exception {
        QuestForm questFormWithOnlySecondPageError = getQuestFormWithOnlySecondPageError();

        when(accountService.findAccountById(anyInt())).thenReturn(getMyAccount());
        when(questService.existQuestNameByCompany(anyString(), anyInt())).thenReturn(Boolean.FALSE);
        when(slotListValidator.checkByType(any(), any())).thenReturn("errorMessage");
        when(slotListJsonMapper.toJSON(ArgumentMatchers.any(SlotList.class))).thenReturn("");
        when(accountService.findAllAccountsInCompanyByOwnAccountId(anyInt())).thenReturn(List.of(getAccount(), getMyAccount()));

        mockMvc.perform(post("/quests/save-new-quest")
                .flashAttr("questForm", questFormWithOnlySecondPageError)
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("questForm", "typeBuilds", "slotListJSON", "defaultStatuses", "allAccounts")
        ).andExpect(model().attribute("questForm", is(questFormWithOnlySecondPageError))
        ).andExpect(model().attribute("typeBuilds", is(SlotListTypeBuild.values()))
        ).andExpect(model().attribute("slotListJSON", notNullValue())
        ).andExpect(model().attribute("defaultStatuses", hasSize(5))
        ).andExpect(model().attribute("allAccounts", hasSize(2))
        ).andExpect(view().name("quests/add-quest-form")
        ).andExpect(status().isOk());

        verify(slotListFactory, never()).makeByType(ArgumentMatchers.any(SlotList.class), ArgumentMatchers.any(SlotListTypeBuild.class));
        verify(questService, never()).saveNewQuest(ArgumentMatchers.any(Quest.class), ArgumentMatchers.any(Account.class));
    }

    @Test
    void showQuest_success() throws Exception {
        Quest quest = getQuest();
        List<Account> accounts = List.of(getMyAccount());

        when(questService.findById(anyInt())).thenReturn(quest);
        when(accountService.getAccountsByQuestId(anyInt())).thenReturn(accounts);
        when(slotListJsonMapper.toObject(anyString())).thenReturn(new SlotList());

        mockMvc.perform(post("/quests/quest-info")
                .param("quest", "1")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("quest", "accounts", "allSlotList")
        ).andExpect(model().attribute("quest", is(quest))
        ).andExpect(model().attribute("accounts", is(accounts))
        ).andExpect(model().attribute("allSlotList", notNullValue())
        ).andExpect(view().name("quests/quest-info")
        ).andExpect(status().isOk());
    }

    @Test
    void showQuest_principalUserRole_failure() throws Exception {
        mockMvc.perform(post("/quests/quest-info")
                .param("quest", "1")
                .with(user(getPrincipalWithUserRole()))
                .with(csrf())
        ).andExpect(status().isForbidden());
    }

    @Test
    void showQuest_strangerQuest_failure() throws Exception {
        when(questService.findById(anyInt())).thenReturn(getStrangerQuest());

        mockMvc.perform(post("/quests/quest-info")
                .param("quest", "2")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(status().isForbidden());
    }

    @Test
    void deleteQuest_success() throws Exception {
        when(questService.findById(anyInt())).thenReturn(getQuest());
        when(reservationService.hasReservationsByQuest(any(Quest.class))).thenReturn(Boolean.FALSE);

        mockMvc.perform(post("/quests/delete")
                .param("quest", "1")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(view().name("redirect:/quests/")
        ).andExpect(status().is3xxRedirection());

        verify(questService).deleteQuest(any(Quest.class));
    }

    @Test
    void deleteQuest_hasReservations_success() throws Exception {
        Quest quest = getQuest();
        when(questService.findById(anyInt())).thenReturn(quest);
        when(reservationService.hasReservationsByQuest(any(Quest.class))).thenReturn(Boolean.TRUE);

        mockMvc.perform(post("/quests/delete")
                .param("quest", "1")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("quest")
        ).andExpect(model().attribute("quest", is(quest))
        ).andExpect(view().name("quests/question-delete-quest")
        ).andExpect(status().isOk());

        verify(questService, never()).deleteQuest(any(Quest.class));
    }

    @Test
    void deleteQuest_strangerQuest_failure() throws Exception {
        when(questService.findById(anyInt())).thenReturn(getStrangerQuest());

        mockMvc.perform(post("/quests/delete")
                .param("quest", "2")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(questService, never()).deleteQuest(any(Quest.class));
    }

    @Test
    void deleteQuest_adminRole_failure() throws Exception {
        when(questService.findById(anyInt())).thenReturn(getQuest());

        mockMvc.perform(post("/quests/delete")
                .param("quest", "1")
                .with(user(getPrincipalWithAdminRole()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(questService, never()).deleteQuest(any(Quest.class));
    }

    @Test
    void deleteQuestFinal_success() throws Exception {
        when(questService.findById(anyInt())).thenReturn(getQuest());

        mockMvc.perform(post("/quests/delete-final")
                .param("quest", "1")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(view().name("redirect:/quests/")
        ).andExpect(status().is3xxRedirection());

        verify(questService).deleteQuest(any(Quest.class));
    }

    @Test
    void deleteQuestFinal_strangerQuest_failure() throws Exception {
        when(questService.findById(anyInt())).thenReturn(getStrangerQuest());

        mockMvc.perform(post("/quests/delete-final")
                .param("quest", "2")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(questService, never()).deleteQuest(any(Quest.class));
    }

    @Test
    void deleteQuestFinal_adminRole_failure() throws Exception {
        when(questService.findById(anyInt())).thenReturn(getQuest());

        mockMvc.perform(post("/quests/delete-final")
                .param("quest", "1")
                .with(user(getPrincipalWithAdminRole()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(questService, never()).deleteQuest(any(Quest.class));
    }

    private AccountUserDetails getPrincipal() {
        return new AccountUserDetails(
                1,
                Account.Role.ROLE_OWNER,
                1,
                Set.of(1),
                "admin@gmail.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority(Account.Role.ROLE_OWNER.name()))
        );
    }

    private AccountUserDetails getPrincipalWithUserRole() {
        return new AccountUserDetails(
                1,
                Account.Role.ROLE_USER,
                1,
                Set.of(1),
                "admin@gmail.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority(Account.Role.ROLE_USER.name()))
        );
    }

    private AccountUserDetails getPrincipalWithAdminRole() {
        return new AccountUserDetails(
                1,
                Account.Role.ROLE_ADMIN,
                1,
                Set.of(1),
                "admin@gmail.com",
                "password",
                Set.of(new SimpleGrantedAuthority(Account.Role.ROLE_ADMIN.name()))
        );
    }

    private Account getAccount() {
        return Account.builder()
                .id(2)
                .login("acc@gmail.com")
                .password("password")
                .firstName("Jord")
                .role(Account.Role.ROLE_ADMIN)
                .companyId(1)
                .quests(Set.of(Quest.builder().id(1).build()))
                .build();
    }

    private Account getMyAccount() {
        return Account.builder()
                .id(1)
                .login("admin@gmail.com")
                .password("password")
                .role(Account.Role.ROLE_OWNER)
                .companyId(1)
                .quests(Set.of(Quest.builder().id(1).build()))
                .build();
    }

    private Account getAccountDiffCompany() {
        return Account.builder()
                .id(1)
                .login("admin@gmail.com")
                .password("password")
                .role(Account.Role.ROLE_OWNER)
                .companyId(2)
                .quests(Set.of(Quest.builder().id(1).build()))
                .build();
    }

    private QuestForm getQuestForm() {
        QuestForm questForm = new QuestForm();
        questForm.setQuestName("Quest name");
        questForm.setMinPersons(1);
        questForm.setMaxPersons(5);
        questForm.setStatuses(Status.MANDATORY_STATUSES);
        questForm.setAutoBlock(LocalTime.MIN);
        questForm.setTypeBuild(SlotListTypeBuild.EQUAL_DAYS);
        questForm.setAccounts(new ArrayList<>(List.of(getMyAccount())));
        questForm.setSlotList(new SlotList());
        return questForm;
    }

    private QuestForm getEmptyQuestForm() {
        QuestForm questForm = new QuestForm();
        questForm.setStatuses(Status.MANDATORY_STATUSES);
        questForm.setAutoBlock(LocalTime.MIN);
        questForm.setTypeBuild(SlotListTypeBuild.EQUAL_DAYS);
        questForm.setAccounts(new ArrayList<>(List.of(getMyAccount())));
        questForm.setSlotList(new SlotList());
        return questForm;
    }

    private QuestForm getQuestFormWithAccountDiffCompany() {
        QuestForm questForm = new QuestForm();
        questForm.setQuestName("Quest name");
        questForm.setMinPersons(1);
        questForm.setMaxPersons(5);
        questForm.setStatuses(Status.MANDATORY_STATUSES);
        questForm.setAutoBlock(LocalTime.MIN);
        questForm.setTypeBuild(SlotListTypeBuild.EQUAL_DAYS);
        questForm.setAccounts(new ArrayList<>(List.of(getAccountDiffCompany())));
        questForm.setSlotList(new SlotList());
        return questForm;
    }

    private QuestForm getQuestFormWithOnlySecondPageError() {
        QuestForm questForm = new QuestForm();
        questForm.setQuestName("Quest name");
        questForm.setMinPersons(1);
        questForm.setMaxPersons(5);
        questForm.setOnlySecondPageError(true);
        questForm.setStatuses(Status.MANDATORY_STATUSES);
        questForm.setAutoBlock(LocalTime.MIN);
        questForm.setTypeBuild(SlotListTypeBuild.EQUAL_DAYS);
        questForm.setAccounts(new ArrayList<>(List.of(getMyAccount())));
        questForm.setSlotList(new SlotList());
        return questForm;
    }

    private Quest getQuest() {
        Quest quest = new Quest();
        quest.setId(1);
        quest.setQuestName("Quest name");
        quest.setCompanyId(1);
        quest.setMinPersons(1);
        quest.setMaxPersons(5);
        quest.setStatuses(Status.MANDATORY_STATUSES);
        quest.setAutoBlock(LocalTime.MIN);
        quest.setAccounts(new ArrayList<>(List.of(getMyAccount())));
        quest.setSlotList("");
        return quest;
    }

    private Quest getStrangerQuest() {
        Quest quest = new Quest();
        quest.setId(2);
        quest.setQuestName("Quest name");
        quest.setCompanyId(1);
        quest.setMinPersons(1);
        quest.setMaxPersons(5);
        quest.setStatuses(Status.MANDATORY_STATUSES);
        quest.setAutoBlock(LocalTime.MIN);
        quest.setAccounts(new ArrayList<>(List.of(getMyAccount())));
        quest.setSlotList("");
        return quest;
    }
}