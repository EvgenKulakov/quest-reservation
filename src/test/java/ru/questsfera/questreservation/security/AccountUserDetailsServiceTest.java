package ru.questsfera.questreservation.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.questsfera.questreservation.mapper.AccountMapper;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.repository.jpa.AccountRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountUserDetailsServiceTest {

    @Mock AccountRepository accountRepository;
    @Mock AccountMapper accountMapper;
    @InjectMocks AccountUserDetailsService accountUserDetailsService;

    @Test
    void loadUserByUsername_success() {
        Account account = Mockito.mock(Account.class);
        AccountUserDetails accountUserDetailsExcepted = getSecurityAccount();

        when(accountRepository.findAccountByLoginWithQuests(anyString())).thenReturn(Optional.of(account));
        when(accountMapper.toAccountUserDetails(account)).thenReturn(accountUserDetailsExcepted);
        AccountUserDetails accountUserDetailsActual = (AccountUserDetails) accountUserDetailsService.loadUserByUsername(anyString());

        assertThat(accountUserDetailsActual)
                .usingRecursiveComparison()
                        .isEqualTo(accountUserDetailsExcepted);

        verify(accountRepository).findAccountByLoginWithQuests(anyString());
    }

    @Test
    void loadUserByUsername_failure() {
        String notExistsLogin = "login";
        when(accountRepository.findAccountByLoginWithQuests(notExistsLogin)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountUserDetailsService.loadUserByUsername(notExistsLogin))
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
                Set.of(1),
                "login",
                "password",
                Collections.singleton(new SimpleGrantedAuthority(Account.Role.ROLE_OWNER.name()))
        );
    }
}