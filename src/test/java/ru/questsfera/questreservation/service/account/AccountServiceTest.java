package ru.questsfera.questreservation.service.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.repository.jdbc.AccountJdbcRepository;
import ru.questsfera.questreservation.repository.jpa.AccountRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    static final String ACCOUNT_LOGIN = "admin@gmail.com";
    static final String NOT_EXISTS_LOGIN = "not-exists-login@gmail.com";

    @Mock AccountRepository accountRepository;
    @Mock AccountJdbcRepository accountJdbcRepository;
    @InjectMocks AccountService accountService;

    @Test
    void loadUserByUsername_success() {
        Account account = Mockito.mock(Account.class);
        when(account.getLogin()).thenReturn(ACCOUNT_LOGIN);
        when(account.getPassword()).thenReturn("");
        when(account.getRole()).thenReturn(Account.Role.ROLE_OWNER);

        when(accountRepository.findAccountByLogin(anyString())).thenReturn(Optional.of(account));
        UserDetails userDetails = accountService.loadUserByUsername(anyString());

        assertThat(userDetails)
                .extracting(UserDetails::getUsername, UserDetails::getPassword, UserDetails::getAuthorities)
                .containsExactly(
                        account.getLogin(),
                        account.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority(account.getRole().name()))
                );

        verify(accountRepository).findAccountByLogin(anyString());
    }

    @Test
    void loadUserByUsername_failure() {
        when(accountRepository.findAccountByLogin(NOT_EXISTS_LOGIN)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.loadUserByUsername(NOT_EXISTS_LOGIN))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(String.format("Пользователь %s не найден", NOT_EXISTS_LOGIN));
    }

    @Test
    void getAccountByLogin_success() {
        Account exceptedAccount = Mockito.mock(Account.class);
        when(accountRepository.findAccountByLogin(anyString())).thenReturn(Optional.of(exceptedAccount));
        Account actualAccount = accountService.getAccountByLogin(anyString());

        assertThat(actualAccount).isSameAs(exceptedAccount);

        verify(accountRepository).findAccountByLogin(anyString());
    }

    @Test
    void getAccountByLogin_failure() {
        when(accountRepository.findAccountByLogin(NOT_EXISTS_LOGIN)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.getAccountByLogin(NOT_EXISTS_LOGIN))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(String.format("Пользователь %s не найден", NOT_EXISTS_LOGIN));
    }

    @Test
    void findAccountByLoginWithQuests_success() {
        Account exceptedAccount = Mockito.mock(Account.class);

        when(accountRepository.findAccountByLoginWithQuests(anyString())).thenReturn(Optional.of(exceptedAccount));
        Account actualAccount = accountService.findAccountByLoginWithQuests(anyString());

        assertThat(actualAccount).isSameAs(exceptedAccount);

        verify(accountRepository).findAccountByLoginWithQuests(anyString());
    }

    @Test
    void findAllAccountsByCompanyId_success() {
        List<Account> exceptedAccounts = List.of(Mockito.mock(Account.class));
        when(accountRepository.findAllByCompanyId(anyInt())).thenReturn(exceptedAccounts);
        List<Account> actualAccounts = accountService.findAllAccountsByCompanyId(anyInt());

        assertThat(actualAccounts).isSameAs(exceptedAccounts);

        verify(accountRepository).findAllByCompanyId(anyInt());
    }

    @Test
    void findAllAccountsByCompanyId_empty() {
        when(accountRepository.findAllByCompanyId(anyInt())).thenReturn(new ArrayList<>());
        List<Account> actualAccounts = accountService.findAllAccountsByCompanyId(anyInt());

        assertThat(actualAccounts.isEmpty()).isTrue();

        verify(accountRepository).findAllByCompanyId(anyInt());
    }

    @Test
    void findAllAccountsInCompanyByOwnAccountName_success() {
        List<Account> exceptedAccounts = List.of(Mockito.mock(Account.class));
        when(accountJdbcRepository.findAllAccountsInCompanyByOwnAccountName(anyString())).thenReturn(exceptedAccounts);
        List<Account> actualAccounts = accountService.findAllAccountsInCompanyByOwnAccountName(anyString());

        assertThat(actualAccounts).isSameAs(exceptedAccounts);

        verify(accountJdbcRepository).findAllAccountsInCompanyByOwnAccountName(anyString());
    }

    @Test
    void findAllAccountsInCompanyByOwnAccountName_empty() {
        when(accountJdbcRepository.findAllAccountsInCompanyByOwnAccountName(anyString()))
                .thenReturn(new ArrayList<>());
        List<Account> actualAccounts = accountService.findAllAccountsInCompanyByOwnAccountName(anyString());

        assertThat(actualAccounts.isEmpty()).isTrue();

        verify(accountJdbcRepository).findAllAccountsInCompanyByOwnAccountName(anyString());
    }

    @Test
    void findOwnAccountsByAccountName_success() {
        Account account = Mockito.mock(Account.class);
        List<Account> exceptedAccounts = List.of(account);
        when(accountRepository.findAccountByLogin(anyString())).thenReturn(Optional.of(account));
        when(accountJdbcRepository.findOwnAccountsByMyAccountOrderByName(account)).thenReturn(exceptedAccounts);
        List<Account> actualAccounts = accountService.findOwnAccountsByAccountName(anyString());

        assertThat(actualAccounts).isSameAs(exceptedAccounts);

        verify(accountJdbcRepository).findOwnAccountsByMyAccountOrderByName(account);
    }

    @Test
    void findOwnAccountsByAccountName_empty() {
        Account account = Mockito.mock(Account.class);
        when(accountRepository.findAccountByLogin(anyString())).thenReturn(Optional.of(account));
        when(accountJdbcRepository.findOwnAccountsByMyAccountOrderByName(account)).thenReturn(new ArrayList<>());
        List<Account> actualAccounts = accountService.findOwnAccountsByAccountName(anyString());

        assertThat(actualAccounts.isEmpty()).isTrue();

        verify(accountJdbcRepository).findOwnAccountsByMyAccountOrderByName(account);
    }

    @Test
    void getAccountsByQuest_success() {
        Quest quest = Mockito.mock(Quest.class);
        Integer questId = 1;
        List<Account> exceptedAccounts = List.of(Mockito.mock(Account.class));

        when(quest.getId()).thenReturn(questId);
        when(accountRepository.findAllByQuestIdOrderByName(anyInt())).thenReturn(exceptedAccounts);
        List<Account> actualAccounts = accountService.getAccountsByQuest(quest);

        assertThat(actualAccounts).isSameAs(exceptedAccounts);

        verify(accountRepository).findAllByQuestIdOrderByName(anyInt());
    }

    @Test
    void getAccountsByQuest_failure() {
        Quest quest = Mockito.mock(Quest.class);
        Integer questId = 1;

        when(quest.getId()).thenReturn(questId);
        when(accountRepository.findAllByQuestIdOrderByName(anyInt())).thenReturn(new ArrayList<>());
        List<Account> actualAccounts = accountService.getAccountsByQuest(quest);

        assertThat(actualAccounts.isEmpty()).isTrue();

        verify(accountRepository).findAllByQuestIdOrderByName(anyInt());
    }

    @Test
    void existAccountByLogin() {
        when(accountRepository.existsAccountByLogin(ACCOUNT_LOGIN)).thenReturn(Boolean.TRUE);
        boolean existsAccount = accountService.existAccountByLogin(ACCOUNT_LOGIN);

        assertThat(existsAccount).isTrue();

        verify(accountRepository).existsAccountByLogin(ACCOUNT_LOGIN);
    }

    @Test
    void saveAccount() {
        Account account = Mockito.mock(Account.class);
        accountService.saveAccount(account);
        verify(accountRepository).save(account);
    }

    @Test
    void deleteById() {
        accountService.deleteById(anyInt());
        verify(accountRepository).deleteById(anyInt());
    }
}