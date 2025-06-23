package ru.questsfera.questreservation.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.questsfera.questreservation.mapper.AccountMapper;
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
class SecurityAccountServiceTest {

    @Mock AccountService accountService;
    @Mock AccountMapper accountMapper;
    @InjectMocks SecurityAccountService securityAccountService;

    @Test
    void loadUserByUsername_success() {
        Account account = getAccount();
        AccountUserDetails accountUserDetailsExcepted = getSecurityAccount();

        when(accountService.getAccountByLogin(anyString())).thenReturn(account);
        when(accountMapper.toAccountUserDetails(account)).thenReturn(accountUserDetailsExcepted);
        AccountUserDetails accountUserDetailsActual = (AccountUserDetails) securityAccountService.loadUserByUsername(anyString());

        assertThat(accountUserDetailsActual)
                .usingRecursiveComparison()
                        .isEqualTo(accountUserDetailsExcepted);

        verify(accountService).getAccountByLogin(anyString());
    }

    @Test
    void loadUserByUsername_failure() {
        String notExistsLogin = "login";
        when(accountService.getAccountByLogin(notExistsLogin)).thenThrow(
                new UsernameNotFoundException(format("Пользователь %s не найден", notExistsLogin)));

        assertThatThrownBy(() -> securityAccountService.loadUserByUsername(notExistsLogin))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(format("Пользователь %s не найден", notExistsLogin));
    }

    private Account getAccount() {
        return Account.builder()
                .id(1)
                .login("login")
                .password("password")
                .role(Account.Role.ROLE_OWNER)
                .companyId(1)
                .build();
    }

    private AccountUserDetails getSecurityAccount() {
        return new AccountUserDetails(
                1,
                Account.Role.ROLE_OWNER,
                1,
                "login",
                "password",
                Collections.singleton(new SimpleGrantedAuthority(Account.Role.ROLE_OWNER.name()))
        );
    }
}