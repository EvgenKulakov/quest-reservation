package ru.questsfera.questreservation.service.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.repository.jdbc.AccountJdbcRepository;
import ru.questsfera.questreservation.repository.jpa.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock AccountRepository accountRepository;
    @Mock AccountJdbcRepository accountJdbcRepository;
    @InjectMocks AccountService accountService;

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
    void getAccountsByQuestId_success() {
        List<Account> exceptedAccounts = List.of(Mockito.mock(Account.class));

        when(accountRepository.findAllByQuestIdOrderByName(anyInt())).thenReturn(exceptedAccounts);
        List<Account> actualAccounts = accountService.getAccountsByQuestId(anyInt());

        assertThat(actualAccounts).isSameAs(exceptedAccounts);

        verify(accountRepository).findAllByQuestIdOrderByName(anyInt());
    }

    @Test
    void getAccountsByQuestId_failure() {
        when(accountRepository.findAllByQuestIdOrderByName(anyInt())).thenReturn(new ArrayList<>());
        List<Account> actualAccounts = accountService.getAccountsByQuestId(anyInt());

        assertThat(actualAccounts.isEmpty()).isTrue();

        verify(accountRepository).findAllByQuestIdOrderByName(anyInt());
    }

    @Test
    void existAccountByLogin() {
        String accountLogin = "admin@gmail.com";
        when(accountRepository.existsAccountByLogin(accountLogin)).thenReturn(Boolean.TRUE);
        boolean existsAccount = accountService.existAccountByLogin(accountLogin);

        assertThat(existsAccount).isTrue();

        verify(accountRepository).existsAccountByLogin(accountLogin);
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