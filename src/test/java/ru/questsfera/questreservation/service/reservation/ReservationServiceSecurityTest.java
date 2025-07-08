package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.questsfera.questreservation.config.Profile;
import ru.questsfera.questreservation.model.dto.ReservationWithClient;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.security.AccountUserDetails;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles(Profile.H2)
@Sql(scripts = {"classpath:common_test_data.sql"})
class ReservationServiceSecurityTest {

    @Autowired
    ReservationService reservationService;

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    void findReservationWIthClientById_ok() {
        ReservationWithClient reservation = reservationService.findReservationWIthClientById(1L);
        assertThat(reservation).isNotNull();
    }

    @Test
    @WithUserDetails(value = "third@gmail.com")
    void findReservationWIthClientById_forbidden() {
        assertThatThrownBy(() -> reservationService.findReservationWIthClientById(8L))
                .isInstanceOf(AuthorizationDeniedException.class);
    }

    private AccountUserDetails getAccountUserDetails() {
        return new AccountUserDetails(
                1,
                Account.Role.ROLE_OWNER,
                1,
                Set.of(1),
                "admin@gmail.com",
                "password",
                Collections.singleton(new SimpleGrantedAuthority(Account.Role.ROLE_ADMIN.name()))
        );
    }

    private ReservationWithClient getReservation() {
        return ReservationWithClient.builder()
                .id(1L)
                .questId(1)
                .build();
    }
}
