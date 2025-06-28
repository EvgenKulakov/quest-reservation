package ru.questsfera.questreservation.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.mapper.AccountMapper;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.repository.jdbc.AccountJdbcRepository;
import ru.questsfera.questreservation.repository.jpa.AccountRepository;
import ru.questsfera.questreservation.security.AccountUserDetailsService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountJdbcRepository accountJdbcRepository;
    private final AccountMapper accountMapper;
    private final AccountUserDetailsService accountUserDetailsService;

    @Transactional(readOnly = true)
    public Account findAccountByLogin(String login) {
        Optional<Account> accountOptional = accountRepository.findAccountByLogin(login);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        }
        throw new UsernameNotFoundException(String.format("Пользователь %s не найден", login));
    }

    @Transactional(readOnly = true)
    public Account findAccountByLoginWithQuests(String login) {
        Optional<Account> accountOptional = accountRepository.findAccountByLoginWithQuests(login);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        }
        throw new UsernameNotFoundException(String.format("Пользователь %s не найден", login));
    }

    @Transactional(readOnly = true)
    public Account findAccountById(Integer accountId) {
        return accountRepository.findById(accountId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Account findAccountByIdWithQuests(Integer accountId) {
        return accountRepository.findAccountByIdWithQuests(accountId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<Account> findAllAccountsByCompanyId(Integer companyId) {
        return accountRepository.findAllByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public List<Account> findAllAccountsInCompanyByOwnAccountId(Integer accountId) {
        return accountJdbcRepository.findAllAccountsInCompanyByOwnAccountId(accountId);
    }

    @Transactional(readOnly = true)
    public List<Account> findOwnAccountsByAccountId(Integer accountId) {
        Account myAccount = accountRepository.findById(accountId).orElseThrow();
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

    @Transactional
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    public void updateCurrentAccount(Account account) {
        accountRepository.save(account);
        accountUserDetailsService.updateAccountUserDetails(accountMapper.toAccountUserDetails(account));
    }

    @Transactional
    public void deleteById(Integer id) {
        accountRepository.deleteById(id);
    }
}
