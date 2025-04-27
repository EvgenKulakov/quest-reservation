package ru.questsfera.questreservation.service.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        Account accountOwner = getAccount();
        when(accountRepository.findAccountByLogin(ACCOUNT_LOGIN)).thenReturn(Optional.of(accountOwner));
        UserDetails userDetails = accountService.loadUserByUsername(ACCOUNT_LOGIN);

        assertThat(userDetails)
                .extracting(UserDetails::getUsername, UserDetails::getPassword, UserDetails::getAuthorities)
                .containsExactly(
                        accountOwner.getLogin(),
                        accountOwner.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority(accountOwner.getRole().name()))
                );

        verify(accountRepository).findAccountByLogin(ACCOUNT_LOGIN);
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
        Account exceptedAccount = getAccount();
        when(accountRepository.findAccountByLogin(ACCOUNT_LOGIN)).thenReturn(Optional.of(exceptedAccount));
        Account actualAccount = accountService.getAccountByLogin(ACCOUNT_LOGIN);

        assertThat(actualAccount).isSameAs(exceptedAccount);

        verify(accountRepository).findAccountByLogin(ACCOUNT_LOGIN);
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
        Account exceptedAccount = getAccount();
        when(accountRepository.findAccountByLoginWithQuests(ACCOUNT_LOGIN))
                .thenReturn(Optional.of(exceptedAccount));
        Account actualAccount = accountService.findAccountByLoginWithQuests(ACCOUNT_LOGIN);

        assertThat(actualAccount).isSameAs(exceptedAccount);

        verify(accountRepository).findAccountByLoginWithQuests(ACCOUNT_LOGIN);
    }

    @Test
    void findAllAccountsByCompanyId_success() {
        List<Account> exceptedAccounts = List.of(getAccount());
        when(accountRepository.findAllByCompanyId(1)).thenReturn(exceptedAccounts);
        List<Account> actualAccounts = accountService.findAllAccountsByCompanyId(1);

        assertThat(actualAccounts).isSameAs(exceptedAccounts);

        verify(accountRepository).findAllByCompanyId(1);
    }

    @Test
    void findAllAccountsByCompanyId_empty() {
        when(accountRepository.findAllByCompanyId(100)).thenReturn(new ArrayList<>());
        List<Account> actualAccounts = accountService.findAllAccountsByCompanyId(100);

        assertThat(actualAccounts.isEmpty()).isTrue();

        verify(accountRepository).findAllByCompanyId(100);
    }

    @Test
    void findAllAccountsInCompanyByOwnAccountName_success() {
        List<Account> exceptedAccounts = List.of(getAccount());
        when(accountJdbcRepository.findAllAccountsInCompanyByOwnAccountName(ACCOUNT_LOGIN)).thenReturn(exceptedAccounts);
        List<Account> actualAccounts = accountService.findAllAccountsInCompanyByOwnAccountName(ACCOUNT_LOGIN);

        assertThat(actualAccounts).isSameAs(exceptedAccounts);

        verify(accountJdbcRepository).findAllAccountsInCompanyByOwnAccountName(ACCOUNT_LOGIN);
    }

    @Test
    void findAllAccountsInCompanyByOwnAccountName_empty() {
        when(accountJdbcRepository.findAllAccountsInCompanyByOwnAccountName(NOT_EXISTS_LOGIN))
                .thenReturn(new ArrayList<>());
        List<Account> actualAccounts = accountService.findAllAccountsInCompanyByOwnAccountName(NOT_EXISTS_LOGIN);

        assertThat(actualAccounts.isEmpty()).isTrue();

        verify(accountJdbcRepository).findAllAccountsInCompanyByOwnAccountName(NOT_EXISTS_LOGIN);
    }

    @Test
    void findOwnAccountsByAccountName_success() {
        Account account = getAccount();
        List<Account> exceptedAccounts = List.of(account);
        when(accountRepository.findAccountByLogin(ACCOUNT_LOGIN)).thenReturn(Optional.of(account));
        when(accountJdbcRepository.findOwnAccountsByMyAccountOrderByName(account)).thenReturn(exceptedAccounts);
        List<Account> actualAccounts = accountService.findOwnAccountsByAccountName(ACCOUNT_LOGIN);

        assertThat(actualAccounts).isSameAs(exceptedAccounts);

        verify(accountJdbcRepository).findOwnAccountsByMyAccountOrderByName(account);
    }

    @Test
    void findOwnAccountsByAccountName_empty() {
        Account account = getAccount();
        when(accountRepository.findAccountByLogin(ACCOUNT_LOGIN)).thenReturn(Optional.of(account));
        when(accountJdbcRepository.findOwnAccountsByMyAccountOrderByName(account)).thenReturn(new ArrayList<>());
        List<Account> actualAccounts = accountService.findOwnAccountsByAccountName(ACCOUNT_LOGIN);

        assertThat(actualAccounts.isEmpty()).isTrue();

        verify(accountJdbcRepository).findOwnAccountsByMyAccountOrderByName(account);
    }

    @Test
    void getAccountsByQuest_success() {
        List<Account> exceptedAccounts = List.of(getAccount());
        when(accountRepository.findAllByQuestIdOrderByName(1)).thenReturn(exceptedAccounts);
        List<Account> actualAccounts = accountService.getAccountsByQuest(getQuest());

        assertThat(actualAccounts).isSameAs(exceptedAccounts);

        verify(accountRepository).findAllByQuestIdOrderByName(1);
    }

    @Test
    void getAccountsByQuest_failure() {
        when(accountRepository.findAllByQuestIdOrderByName(1)).thenReturn(new ArrayList<>());
        List<Account> actualAccounts = accountService.getAccountsByQuest(getQuest());

        assertThat(actualAccounts.isEmpty()).isTrue();

        verify(accountRepository).findAllByQuestIdOrderByName(1);
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
        Account account = getAccount();
        accountService.saveAccount(account);
        verify(accountRepository).save(account);
    }

    @Test
    void deleteById() {
        accountService.deleteById(1);
        verify(accountRepository).deleteById(1);
    }

    private Account getAccount() {
        return Account.builder()
                .id(1)
                .login(ACCOUNT_LOGIN)
                .password("$2a$10$I6WnbfYRb2Z8uBysTKy5l.uSazvJYhqFgsj4LQ.5vZc65TmGlcat6")
                .firstName("Test")
                .lastName("Евгений")
                .phone("+79995554433")
                .role(Account.Role.ROLE_OWNER)
                .companyId(1)
                .build();
    }

    private Quest getQuest() {
        return Quest.builder()
                .id(1)
                .questName("Quest One")
                .build();
    }
}