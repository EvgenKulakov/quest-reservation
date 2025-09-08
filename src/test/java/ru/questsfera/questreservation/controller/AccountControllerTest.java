package ru.questsfera.questreservation.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.security.AccountUserDetails;
import ru.questsfera.questreservation.security.DomainPermissionEvaluator;
import ru.questsfera.questreservation.security.PasswordGenerator;
import ru.questsfera.questreservation.security.SecurityConfig;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.quest.QuestService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@Import({SecurityConfig.class, DomainPermissionEvaluator.class})
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean AccountService accountService;
    @MockitoBean QuestService questService;
    @MockitoBean PasswordGenerator passwordGenerator;

    @Test
    void showAccountsList_success() throws Exception {
        when(accountService.findOwnAccountsByAccountId(anyInt())).thenReturn(List.of(new Account()));

        mockMvc.perform(get("/accounts/")
                .with(user(getPrincipal()))
        ).andExpect(model().attributeExists("accounts")
        ).andExpect(model().attribute("accounts", hasSize(1))
        ).andExpect(view().name("accounts/accounts-list")
        ).andExpect(status().isOk());
    }

    @Test
    void showAccountsList_forbidden() throws Exception {
        mockMvc.perform(get("/accounts/")
                .with(user(getPrincipalWithUserRole()))
        ).andExpect(status().isForbidden());
    }

    @Test
    void addAccountForm() throws Exception {
        when(accountService.findAccountByIdWithQuests(anyInt())).thenReturn(getAccount());

        mockMvc.perform(get("/accounts/add-form")
                .with(user(getPrincipal()))
        ).andExpect(model().attributeExists("account", "allQuests", "roles")
        ).andExpect(model().attribute("account", notNullValue())
        ).andExpect(model().attribute("allQuests", hasSize(1))
        ).andExpect(model().attribute("roles", not(hasItem(Account.Role.ROLE_OWNER)))
        ).andExpect(view().name("accounts/account-form")
        ).andExpect(status().isOk());
    }

    @Test
    void updateAccount_success() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccount());
        when(questService.getQuestsByCompany(anyInt())).thenReturn(List.of(new Quest()));

        mockMvc.perform(post("/accounts/update-form")
                .param("account", "2")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("account", "allQuests", "roles")
        ).andExpect(model().attribute("account", is(getAccount()))
        ).andExpect(model().attribute("allQuests", hasSize(1))
        ).andExpect(model().attribute("roles", not(hasItem(Account.Role.ROLE_OWNER)))
        ).andExpect(view().name("accounts/account-form")
        ).andExpect(status().isOk());
    }

    @Test
    void updateAccount_diffCompany_forbidden() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccountWithDiffCompany());

        mockMvc.perform(post("/accounts/update-form")
                .param("account", "2")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(status().isForbidden());
    }

    @Test
    void updateAccount_principalAdminRole_forbidden() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccount());

        mockMvc.perform(post("/accounts/update-form")
                .param("account", "2")
                .with(user(getPrincipalWithAdminRole()))
                .with(csrf())
        ).andExpect(status().isForbidden());
    }

    @Test
    void saveAccount_updateAccount_success() throws Exception {
        when(accountService.existAccountByLogin(anyString())).thenReturn(Boolean.FALSE);

        mockMvc.perform(post("/accounts/save-account")
                .flashAttr("account", getAccount())
                .param("oldLogin", "oldLogin")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(view().name("redirect:/accounts/")
        ).andExpect(status().is3xxRedirection());

        verify(passwordGenerator, never()).createPasswordHash(ArgumentMatchers.anyString());
        verify(accountService).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveAccount_newAccount_success() throws Exception {
        when(accountService.existAccountByLogin(anyString())).thenReturn(Boolean.FALSE);

        mockMvc.perform(post("/accounts/save-account")
                .flashAttr("account", getAccountWithoutId())
                .param("oldLogin", "oldLogin")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(view().name("redirect:/accounts/")
        ).andExpect(status().is3xxRedirection());

        verify(passwordGenerator).createPasswordHash(ArgumentMatchers.anyString());
        verify(accountService).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveAccount_principalAdminRole_forbidden() throws Exception {
        mockMvc.perform(post("/accounts/save-account")
                .flashAttr("account", getAccount())
                .param("oldLogin", "oldLogin")
                .with(user(getPrincipalWithAdminRole()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(accountService, never()).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveAccount_strangersQuests_forbidden() throws Exception {
        mockMvc.perform(post("/accounts/save-account")
                .flashAttr("account", getAccountWithStrangersQuests())
                .param("oldLogin", "oldLogin")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(accountService, never()).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveAccount_existsUsername_failure() throws Exception {
        when(accountService.existAccountByLogin(anyString())).thenReturn(Boolean.TRUE);
        when(questService.getQuestsByCompany(anyInt())).thenReturn(List.of(new Quest()));

        mockMvc.perform(post("/accounts/save-account")
                .flashAttr("account", getAccount())
                .param("oldLogin", "oldLogin")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("account", "allQuests", "roles")
        ).andExpect(model().attribute("account", is(getAccount()))
        ).andExpect(model().attribute("allQuests", hasSize(1))
        ).andExpect(model().attribute("roles", not(hasItem(Account.Role.ROLE_OWNER)))
        ).andExpect(view().name("accounts/account-form")
        ).andExpect(status().isOk());

        verify(accountService, never()).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveAccount_loginError_failure() throws Exception {
        when(accountService.existAccountByLogin(anyString())).thenReturn(Boolean.FALSE);
        when(questService.getQuestsByCompany(anyInt())).thenReturn(List.of(new Quest()));

        mockMvc.perform(post("/accounts/save-account")
                .flashAttr("account", getAccountWithLoginError())
                .param("oldLogin", "oldLogin")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("account", "allQuests", "roles")
        ).andExpect(model().attribute("account", is(getAccountWithLoginError()))
        ).andExpect(model().attribute("allQuests", hasSize(1))
        ).andExpect(model().attribute("roles", not(hasItem(Account.Role.ROLE_OWNER)))
        ).andExpect(view().name("accounts/account-form")
        ).andExpect(status().isOk());

        verify(accountService, never()).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveAccount_passwordError_failure() throws Exception {
        when(questService.getQuestsByCompany(anyInt())).thenReturn(List.of(new Quest()));

        mockMvc.perform(post("/accounts/save-account")
                .flashAttr("account", getAccountWithPasswordError())
                .param("oldLogin", "admin@gmail.com")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("account", "allQuests", "roles")
        ).andExpect(model().attribute("account", is(getAccountWithPasswordError()))
        ).andExpect(model().attribute("allQuests", hasSize(1))
        ).andExpect(model().attribute("roles", not(hasItem(Account.Role.ROLE_OWNER)))
        ).andExpect(view().name("accounts/account-form")
        ).andExpect(status().isOk());

        verify(accountService, never()).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveAccount_firstNameError_failure() throws Exception {
        when(questService.getQuestsByCompany(anyInt())).thenReturn(List.of(new Quest()));

        mockMvc.perform(post("/accounts/save-account")
                .flashAttr("account", getAccountWithFirstNameError())
                .param("oldLogin", "admin@gmail.com")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("account", "allQuests", "roles")
        ).andExpect(model().attribute("account", is(getAccountWithFirstNameError()))
        ).andExpect(model().attribute("allQuests", hasSize(1))
        ).andExpect(model().attribute("roles", not(hasItem(Account.Role.ROLE_OWNER)))
        ).andExpect(view().name("accounts/account-form")
        ).andExpect(status().isOk());

        verify(accountService, never()).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void updatePassword_success() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccount());

        mockMvc.perform(post("/accounts/update-account-password")
                .param("account", "2")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("account", "newPassword", "errorPassword")
        ).andExpect(model().attribute("account", is(getAccount()))
        ).andExpect(model().attribute("newPassword", is(""))
        ).andExpect(model().attribute("errorPassword", is(false))
        ).andExpect(view().name("accounts/password-form")
        ).andExpect(status().isOk());
    }

    @Test
    void updatePassword_principalAdminRole_forbidden() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccount());

        mockMvc.perform(post("/accounts/update-account-password")
                .param("account", "2")
                .with(user(getPrincipalWithAdminRole()))
                .with(csrf())
        ).andExpect(status().isForbidden());
    }

    @Test
    void updatePassword_diffCompany_forbidden() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccountWithDiffCompany());

        mockMvc.perform(post("/accounts/update-account-password")
                .param("account", "2")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(status().isForbidden());
    }

    @Test
    void saveNewPassword_success() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccount());

        mockMvc.perform(post("/accounts/save-new-password")
                .param("account", "2")
                .param("newPassword", "newPassword")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(view().name("redirect:/accounts/")
        ).andExpect(status().is3xxRedirection());

        verify(passwordGenerator).createPasswordHash(ArgumentMatchers.anyString());
        verify(accountService).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveNewPassword_principalAdminRole_forbidden() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccount());

        mockMvc.perform(post("/accounts/save-new-password")
                .param("account", "2")
                .param("newPassword", "newPassword")
                .with(user(getPrincipalWithAdminRole()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(passwordGenerator, never()).createPasswordHash(ArgumentMatchers.anyString());
        verify(accountService, never()).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveNewPassword_diffCompany_forbidden() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccountWithDiffCompany());

        mockMvc.perform(post("/accounts/save-new-password")
                .param("account", "2")
                .param("newPassword", "newPassword")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(passwordGenerator, never()).createPasswordHash(ArgumentMatchers.anyString());
        verify(accountService, never()).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void saveNewPassword_passwordError_failure() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccount());

        mockMvc.perform(post("/accounts/save-new-password")
                .param("account", "2")
                .param("newPassword", "pass")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("account", "newPassword", "errorPassword")
        ).andExpect(model().attribute("account", is(getAccount()))
        ).andExpect(model().attribute("newPassword", is("pass"))
        ).andExpect(model().attribute("errorPassword", is(true))
        ).andExpect(view().name("accounts/password-form")
        ).andExpect(status().isOk());

        verify(passwordGenerator, never()).createPasswordHash(ArgumentMatchers.anyString());
        verify(accountService, never()).saveAccount(ArgumentMatchers.any(Account.class));
    }

    @Test
    void deleteAccount_success() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccount());

        mockMvc.perform(post("/accounts/delete")
                .param("account", "2")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(view().name("redirect:/accounts/")
        ).andExpect(status().is3xxRedirection());

        verify(accountService).deleteById(ArgumentMatchers.anyInt());
    }

    @Test
    void deleteAccount_principalAdminRole_forbidden() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccount());

        mockMvc.perform(post("/accounts/delete")
                .param("account", "2")
                .with(user(getPrincipalWithAdminRole()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(accountService, never()).deleteById(ArgumentMatchers.anyInt());
    }

    @Test
    void deleteAccount_diffCompany_forbidden() throws Exception {
        when(accountService.findAccountById(anyInt())).thenReturn(getAccountWithDiffCompany());

        mockMvc.perform(post("/accounts/delete")
                .param("account", "2")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(status().isForbidden());

        verify(accountService, never()).deleteById(ArgumentMatchers.anyInt());
    }

    private Account getAccount() {
        return Account.builder()
                .id(2)
                .login("admin@gmail.com")
                .password("password")
                .firstName("Jord")
                .role(Account.Role.ROLE_ADMIN)
                .companyId(1)
                .quests(Set.of(Quest.builder().id(1).build()))
                .build();
    }

    private Account getAccountWithoutId() {
        return Account.builder()
                .login("admin@gmail.com")
                .password("password")
                .firstName("Jord")
                .role(Account.Role.ROLE_ADMIN)
                .companyId(1)
                .quests(Set.of(Quest.builder().id(1).build()))
                .build();
    }

    private Account getAccountWithDiffCompany() {
        return Account.builder()
                .id(2)
                .role(Account.Role.ROLE_ADMIN)
                .companyId(2)
                .build();
    }

    private Account getAccountWithStrangersQuests() {
        return Account.builder()
                .id(2)
                .login("admin@gmail.com")
                .password("password")
                .firstName("Jord")
                .role(Account.Role.ROLE_ADMIN)
                .companyId(1)
                .quests(Set.of(Quest.builder().id(2).build()))
                .build();
    }

    private Account getAccountWithLoginError() {
        return Account.builder()
                .id(2)
                .login("admin")
                .password("password")
                .firstName("Jord")
                .role(Account.Role.ROLE_ADMIN)
                .companyId(1)
                .quests(Set.of(Quest.builder().id(1).build()))
                .build();
    }

    private Account getAccountWithPasswordError() {
        return Account.builder()
                .id(2)
                .login("admin@gmail.com")
                .password("pass")
                .firstName("Jord")
                .role(Account.Role.ROLE_ADMIN)
                .companyId(1)
                .quests(Set.of(Quest.builder().id(1).build()))
                .build();
    }

    private Account getAccountWithFirstNameError() {
        return Account.builder()
                .id(2)
                .login("admin@gmail.com")
                .password("password")
                .firstName(" ")
                .role(Account.Role.ROLE_ADMIN)
                .companyId(1)
                .quests(Set.of(Quest.builder().id(1).build()))
                .build();
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

    private AccountUserDetails getPrincipalWithUserRole() {
        return new AccountUserDetails(
                1,
                Account.Role.ROLE_USER,
                1,
                Set.of(1),
                "admin@gmail.com",
                "password",
                Set.of(new SimpleGrantedAuthority(Account.Role.ROLE_USER.name()))
        );
    }
}