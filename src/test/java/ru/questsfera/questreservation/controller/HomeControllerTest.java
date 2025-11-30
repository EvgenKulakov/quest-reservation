package ru.questsfera.questreservation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.questsfera.questreservation.converter.QuestConverter;
import ru.questsfera.questreservation.mapper.AccountMapper;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.security.AccountUserDetails;
import ru.questsfera.questreservation.security.DomainPermissionEvaluator;
import ru.questsfera.questreservation.security.PasswordGenerator;
import ru.questsfera.questreservation.security.SecurityConfig;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.company.CompanyService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@Import({SecurityConfig.class, DomainPermissionEvaluator.class})
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean QuestConverter questConverter;
    @MockitoBean AccountService accountService;
    @MockitoBean CompanyService companyService;
    @MockitoBean AccountMapper accountMapper;
    @MockitoBean PasswordGenerator passwordGenerator;

    @Test
    void login_get_success() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(view().name("home/login"))
                .andExpect(status().isOk());
    }

    @Test
    void login_post_success() throws Exception {
        mockMvc.perform(post("/login")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(view().name("home/login")
        ).andExpect(status().isOk());
    }

    @Test
    void register() {
    }

    @Test
    void saveNewAccount() {
    }

    @Test
    void homePage() {
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
}