package ru.questsfera.questreservation.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.service.account.AccountService;

import java.util.Collections;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityUserDetailsServiceTest {

    @Mock AccountService accountService;
    @InjectMocks SecurityUserDetailsService securityUserDetailsService;

    @Test
    void loadUserByUsername_success() {
        Account account = Mockito.mock(Account.class);
        when(account.getLogin()).thenReturn("login");
        when(account.getPassword()).thenReturn("password");
        when(account.getRole()).thenReturn(Account.Role.ROLE_OWNER);

        when(accountService.getAccountByLogin(anyString())).thenReturn(account);
        UserDetails userDetails = securityUserDetailsService.loadUserByUsername(anyString());

        assertThat(userDetails)
                .extracting(UserDetails::getUsername, UserDetails::getPassword, UserDetails::getAuthorities)
                .containsExactly(
                        account.getLogin(),
                        account.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority(account.getRole().name()))
                );

        verify(accountService).getAccountByLogin(anyString());
    }

    @Test
    void loadUserByUsername_failure() {
        String notExistsLogin = "login";
        when(accountService.getAccountByLogin(notExistsLogin)).thenThrow(
                new UsernameNotFoundException(format("Пользователь %s не найден", notExistsLogin)));

        assertThatThrownBy(() -> securityUserDetailsService.loadUserByUsername(notExistsLogin))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(format("Пользователь %s не найден", notExistsLogin));
    }
}