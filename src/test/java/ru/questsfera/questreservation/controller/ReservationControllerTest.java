package ru.questsfera.questreservation.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.questsfera.questreservation.config.JacksonConfig;
import ru.questsfera.questreservation.converter.AccountConverter;
import ru.questsfera.questreservation.converter.QuestConverter;
import ru.questsfera.questreservation.model.dto.*;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.session.SlotListPageSession;
import ru.questsfera.questreservation.security.AccountUserDetails;
import ru.questsfera.questreservation.security.DomainPermissionEvaluator;
import ru.questsfera.questreservation.security.SecurityConfig;
import ru.questsfera.questreservation.service.reservation.ReservationGetOperator;
import ru.questsfera.questreservation.service.reservation.ReservationSaveOperator;
import ru.questsfera.questreservation.service.reservation.ReservationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@Import({SecurityConfig.class, DomainPermissionEvaluator.class, JacksonConfig.class})
class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean QuestConverter questConverter;
    @MockitoBean AccountConverter accountConverter;
    @MockitoBean ReservationService reservationService;
    @MockitoBean ReservationSaveOperator reservationSaveOperator;
    @MockitoBean ReservationGetOperator reservationGetOperator;
    @MockitoBean SlotListPageSession slotListPageSession;

    @Test
    void showSlotList() throws Exception {
        when(reservationGetOperator.getQuestsAndSlotsByDate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getSlotListPage());

        mockMvc.perform(get("/reservations/slot-list")
                .param("date", LocalDate.now().toString())
                .with(user(getPrincipal()))
        ).andExpect(model().attributeExists("res_form", "quest_names_and_slots", "use_statuses", "date")
        ).andExpect(model().attribute("res_form", notNullValue())
        ).andExpect(model().attribute("quest_names_and_slots", notNullValue())
        ).andExpect(model().attribute("use_statuses", notNullValue())
        ).andExpect(model().attribute("date", is(LocalDate.now()))
        ).andExpect(view().name("reservations/slot-list")
        ).andExpect(status().isOk());

        verify(reservationGetOperator).getQuestsAndSlotsByDate(ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(AccountUserDetails.class));
        verify(slotListPageSession).setSlotListPage(ArgumentMatchers.any(SlotListPage.class));
    }

    @Test
    void saveReservation_success() throws Exception {
        when(slotListPageSession.getSlotListPage()).thenReturn(getSlotListPage());

        mockMvc.perform(post("/reservations/save-reservation")
                .flashAttr("res_form", getReservationForm())
                .param("slot", "1")
                .param("date", LocalDate.now().toString())
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("date")
        ).andExpect(view().name("redirect:/reservations/slot-list")
        ).andExpect(status().is3xxRedirection());

        verify(reservationSaveOperator).saveUsingResFormAndSlot(ArgumentMatchers.any(ReservationForm.class), ArgumentMatchers.any(Slot.class));
    }

    @Test
    void saveReservation_failure() throws Exception {
        when(slotListPageSession.getSlotListPage()).thenReturn(getSlotListPage());
        when(reservationGetOperator.getQuestsAndSlotsByDate(any(), any())).thenReturn(getSlotListPage());

        mockMvc.perform(post("/reservations/save-reservation")
                .flashAttr("res_form", getReservationFormFailure())
                .param("slot", "1")
                .param("date", LocalDate.now().toString())
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("res_form", "quest_names_and_slots",
                "use_statuses", "date", "error_slot", "change_status", "change_count_persons")
        ).andExpect(model().attribute("res_form", notNullValue())
        ).andExpect(model().attribute("quest_names_and_slots", notNullValue())
        ).andExpect(model().attribute("use_statuses", notNullValue())
        ).andExpect(model().attribute("date", is(LocalDate.now()))
        ).andExpect(model().attribute("error_slot", notNullValue())
        ).andExpect(model().attribute("change_status", notNullValue())
        ).andExpect(model().attribute("change_count_persons", notNullValue())
        ).andExpect(view().name("reservations/slot-list")
        ).andExpect(status().isOk());

        verify(slotListPageSession).setSlotListPage(ArgumentMatchers.any(SlotListPage.class));
        verify(reservationSaveOperator, never()).saveUsingResFormAndSlot(ArgumentMatchers.any(ReservationForm.class), ArgumentMatchers.any(Slot.class));
    }

    @Test
    void blockSlot_success() throws Exception {
        when(slotListPageSession.getSlotListPage()).thenReturn(getSlotListPage());

        mockMvc.perform(post("/reservations/block-slot")
                .flashAttr("res_form", new ReservationForm())
                .param("slot", "1")
                .param("date", LocalDate.now().toString())
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("date")
        ).andExpect(view().name("redirect:/reservations/slot-list")
        ).andExpect(status().is3xxRedirection());

        verify(reservationSaveOperator).saveBlockReservationUsingSlot(ArgumentMatchers.any(Slot.class));
    }

    @Test
    void blockSlot_failure() throws Exception {
        when(slotListPageSession.getSlotListPage()).thenReturn(getSlotListPage());
        when(reservationGetOperator.getQuestsAndSlotsByDate(any(), any())).thenReturn(getSlotListPage());

        mockMvc.perform(post("/reservations/block-slot")
                .flashAttr("res_form", getReservationFormFailure())
                .param("slot", "1")
                .param("date", LocalDate.now().toString())
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(model().attributeExists("res_form", "quest_names_and_slots",
                "use_statuses", "date", "error_slot", "change_status", "change_count_persons")
        ).andExpect(model().attribute("res_form", notNullValue())
        ).andExpect(model().attribute("quest_names_and_slots", notNullValue())
        ).andExpect(model().attribute("use_statuses", notNullValue())
        ).andExpect(model().attribute("date", is(LocalDate.now()))
        ).andExpect(model().attribute("error_slot", notNullValue())
        ).andExpect(model().attribute("change_status", notNullValue())
        ).andExpect(model().attribute("change_count_persons", notNullValue())
        ).andExpect(view().name("reservations/slot-list")
        ).andExpect(status().isOk());

        verify(slotListPageSession).setSlotListPage(ArgumentMatchers.any(SlotListPage.class));
        verify(reservationSaveOperator, never()).saveUsingResFormAndSlot(ArgumentMatchers.any(ReservationForm.class), ArgumentMatchers.any(Slot.class));
    }

    @Test
    void deleteBlockReserve() throws Exception {
        when(slotListPageSession.getSlotListPage()).thenReturn(getSlotListPage());

        mockMvc.perform(post("/reservations/unBlock")
                .param("slot", "1")
                .with(user(getPrincipal()))
                .with(csrf())
        ).andExpect(view().name("redirect:/reservations/slot-list")
        ).andExpect(status().is3xxRedirection());

        verify(reservationService).deleteBlockedReservation(anyLong());
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

    private SlotListPage getSlotListPage() {
        Map<String, List<Slot>> questsAndSlots = Map.of(
                "QuestName",
                List.of(getSlot())
        );

        return new SlotListPage(questsAndSlots, new HashSet<>(Status.DEFAULT_STATUSES));
    }

    private Slot getSlot() {
        return new Slot(
                1,
                1,
                1,
                "QuestName",
                1L,
                LocalDate.now(),
                LocalTime.MIN,
                1000,
                Status.MANDATORY_STATUSES,
                Status.EMPTY,
                2,
                6
        );
    }

    private ReservationForm getReservationForm() {
        ReservationForm reservationForm = new ReservationForm();
        reservationForm.setId(1L);
        reservationForm.setQuestId(1);
        reservationForm.setStatus(Status.NEW_RESERVE);
        reservationForm.setFirstName("name");
        reservationForm.setLastName("last name");
        reservationForm.setPhone("+79995200000");
        reservationForm.setEmail("some@mail.com");
        reservationForm.setCountPersons(5);
        reservationForm.setAdminComment("comment");
        reservationForm.setClientComment("client comment");
        return reservationForm;
    }

    private ReservationForm getReservationFormFailure() {
        ReservationForm reservationForm = new ReservationForm();
        reservationForm.setId(1L);
        reservationForm.setQuestId(1);
        reservationForm.setStatus(Status.NEW_RESERVE);
        reservationForm.setFirstName("name");
        reservationForm.setLastName("last name");
        reservationForm.setPhone("");
        reservationForm.setEmail("some@mail.com");
        reservationForm.setCountPersons(5);
        reservationForm.setAdminComment("comment");
        reservationForm.setClientComment("client comment");
        return reservationForm;
    }
}