package ru.questsfera.questreservation.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.repository.jdbc.AccountJdbcRepository;
import ru.questsfera.questreservation.repository.jpa.AccountRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountJdbcRepository accountJdbcRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = getAccountByLogin(username);
        return new org.springframework.security.core.userdetails.User(
                account.getLogin(),
                account.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(account.getRole().name()))
        );
    }

    @Transactional(readOnly = true)
    public Account getAccountByLogin(String login) {
        Optional<Account> accountOptional = accountRepository.findAccountByLogin(login);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        }
        throw new UsernameNotFoundException(String.format("Пользователь %s не найден", login));
    }

    @Transactional(readOnly = true)
    public Account findAccountByLoginWithQuests(String login) {
        return accountRepository.findAccountByLoginWithQuests(login).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<Account> findAllAccountsByCompanyId(Integer companyId) {
        return accountRepository.findAllByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public List<Account> findAllAccountsInCompanyByOwnAccountName(String accountName) {
        return accountJdbcRepository.findAllAccountsInCompanyByOwnAccountName(accountName);
    }

    @Transactional(readOnly = true)
    public List<Account> findOwnAccountsByAccountName(String accountName) {
        Account myAccount = getAccountByLogin(accountName);
        return accountJdbcRepository.findOwnAccountsByMyAccountOrderByName(myAccount);
    }

    @Transactional(readOnly = true)
    public List<Account> getAccountsByQuest(Quest quest) {
        return accountRepository.findAllByQuestIdOrderByName(quest.getId());
    }

    @Transactional(readOnly = true)
    public boolean existAccountByLogin(String login) {
        if (login.isEmpty()) return false;
        return accountRepository.existsAccountByLogin(login);
    }

    // TODO security
    @Transactional(readOnly = true)
    public boolean existAccountByCompanyId(Account account, Integer companyId) {
        return accountRepository.existsAccountByIdAndCompanyId(account.getId(), companyId);
    }

    @Transactional
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    public void deleteById(Integer id) {
        accountRepository.deleteById(id);
    }

    // TODO security
    @Transactional
    public void checkSecurityForAccount(Account changeAccount, Account myAccount) {
        boolean existAccountByCompany = existAccountByCompanyId(changeAccount, myAccount.getCompanyId());

        boolean haveAccess = myAccount.getRole() == Account.Role.ROLE_OWNER
                || changeAccount.getRole() == Account.Role.ROLE_USER;

        if (!existAccountByCompany || !haveAccess) {
            throw new SecurityException("Нет доступа для изменения данного пользователя");
        }
    }

    // TODO security
    @Transactional
    public void checkSecurityForAccounts(List<Account> changeAccounts, Account myAccount) {
        List<Account> usersByAdmin = findAllAccountsByCompanyId(myAccount.getCompanyId());
        if (!usersByAdmin.containsAll(changeAccounts)) {
            throw new SecurityException("Нет доступа для изменения данных пользователей");
        }
    }
}
