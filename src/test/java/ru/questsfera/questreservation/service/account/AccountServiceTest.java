package ru.questsfera.questreservation.service.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.repository.jdbc.AccountJdbcRepository;
import ru.questsfera.questreservation.repository.jpa.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    void findAccountByLogin_success() {
        Account exceptedAccount = Mockito.mock(Account.class);
        when(accountRepository.findAccountByLogin(anyString())).thenReturn(Optional.of(exceptedAccount));
        Account actualAccount = accountService.findAccountByLogin(anyString());

        assertThat(actualAccount).isSameAs(exceptedAccount);

        verify(accountRepository).findAccountByLogin(anyString());
    }

    @Test
    void findAccountByLogin_failure() {
        when(accountRepository.findAccountByLogin(NOT_EXISTS_LOGIN)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.findAccountByLogin(NOT_EXISTS_LOGIN))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(String.format("Пользователь %s не найден", NOT_EXISTS_LOGIN));
    }

    @Test
    void findAccountById_success() {
        Account exceptedAccount = Mockito.mock(Account.class);
        when(accountRepository.findById(anyInt())).thenReturn(Optional.of(exceptedAccount));
        Account actualAccount = accountService.findAccountById(anyInt());

        assertThat(actualAccount).isSameAs(exceptedAccount);

        verify(accountRepository).findById(anyInt());
    }

    @Test
    void findAccountById_failure() {
        when(accountRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.findAccountById(anyInt()))
                .isInstanceOf(NoSuchElementException.class);
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
    void findAccountByLoginWithQuests_failure() {
        when(accountRepository.findAccountByLoginWithQuests(NOT_EXISTS_LOGIN)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.findAccountByLoginWithQuests(NOT_EXISTS_LOGIN))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(String.format("Пользователь %s не найден", NOT_EXISTS_LOGIN));
    }

    @Test
    void findAccountByIdWithQuests_success() {
        Account exceptedAccount = Mockito.mock(Account.class);

        when(accountRepository.findAccountByIdWithQuests(anyInt())).thenReturn(Optional.of(exceptedAccount));
        Account actualAccount = accountService.findAccountByIdWithQuests(anyInt());

        assertThat(actualAccount).isSameAs(exceptedAccount);

        verify(accountRepository).findAccountByIdWithQuests(anyInt());
    }

    @Test
    void findAccountByIdWithQuests_failure() {
        when(accountRepository.findAccountByIdWithQuests(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.findAccountByIdWithQuests(anyInt()))
                .isInstanceOf(NoSuchElementException.class);
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
    void findAllAccountsInCompanyByOwnAccountId_success() {
        List<Account> exceptedAccounts = List.of(Mockito.mock(Account.class));
        when(accountJdbcRepository.findAllAccountsInCompanyByOwnAccountId(anyInt())).thenReturn(exceptedAccounts);
        List<Account> actualAccounts = accountService.findAllAccountsInCompanyByOwnAccountId(anyInt());

        assertThat(actualAccounts).isSameAs(exceptedAccounts);

        verify(accountJdbcRepository).findAllAccountsInCompanyByOwnAccountId(anyInt());
    }

    @Test
    void findAllAccountsInCompanyByOwnAccountId_empty() {
        when(accountJdbcRepository.findAllAccountsInCompanyByOwnAccountId(anyInt()))
                .thenReturn(new ArrayList<>());
        List<Account> actualAccounts = accountService.findAllAccountsInCompanyByOwnAccountId(anyInt());

        assertThat(actualAccounts.isEmpty()).isTrue();

        verify(accountJdbcRepository).findAllAccountsInCompanyByOwnAccountId(anyInt());
    }

    @Test
    void findOwnAccountsByAccountId_success() {
        Account account = Mockito.mock(Account.class);
        List<Account> exceptedAccounts = List.of(account);
        when(accountRepository.findById(anyInt())).thenReturn(Optional.of(account));
        when(accountJdbcRepository.findOwnAccountsByMyAccountOrderByName(account)).thenReturn(exceptedAccounts);
        List<Account> actualAccounts = accountService.findOwnAccountsByAccountId(anyInt());

        assertThat(actualAccounts).isSameAs(exceptedAccounts);

        verify(accountJdbcRepository).findOwnAccountsByMyAccountOrderByName(account);
    }

    @Test
    void findOwnAccountsByAccountId_empty() {
        Account account = Mockito.mock(Account.class);
        when(accountRepository.findById(anyInt())).thenReturn(Optional.of(account));
        when(accountJdbcRepository.findOwnAccountsByMyAccountOrderByName(account)).thenReturn(new ArrayList<>());
        List<Account> actualAccounts = accountService.findOwnAccountsByAccountId(anyInt());

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