package ru.questsfera.questreservation.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.redis.object.AccountRedis;
import ru.questsfera.questreservation.redis.service.AccountRedisService;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.repository.AccountRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountRedisService accountRedisService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = getAccountByLogin(username);
        return new org.springframework.security.core.userdetails.User(
                account.getEmailLogin(),
                account.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(account.getRole().name()))
        );
    }

    @Transactional
    public Account getAccountByLogin(String emailLogin) {
        Optional<Account> accountOptional = accountRepository.findAccountByEmailLogin(emailLogin);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        }
        throw new UsernameNotFoundException(String.format("Пользователь %s не найден", emailLogin));
    }

    @Transactional
    public List<Account> getAccountsByCompanyId(Integer companyId) {
        return accountRepository.findAllByCompanyIdOrderByEmailLogin(companyId);
    }

    @Transactional
    public List<Account> getAccountsByQuest(Quest quest) {
        return accountRepository.findAllByQuestId(quest.getId());
    }

    @Transactional
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional
    public boolean existAccountByLogin(String emailLogin) {
        if (emailLogin.isEmpty()) return false;
        return accountRepository.existsAccountByEmailLogin(emailLogin);
    }

    @Transactional
    public boolean existAccountByCompanyId(Account account, Integer companyId) {
        return accountRepository.existsAccountByIdAndCompanyId(account.getId(), companyId);
    }

    @Transactional
    public void saveAccount(Account account) {
        try {
            accountRepository.save(account);
            accountRedisService.save(new AccountRedis(account));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Transactional
    public void delete(Account account) {
        accountRepository.delete(account);
        accountRedisService.deleteByEmailLogin(account.getEmailLogin());
    }

    @Transactional
    public void checkSecurityForAccount(Account changeAccount, AccountRedis myAccount) {
        boolean existAccountByCompany = existAccountByCompanyId(changeAccount, myAccount.getCompanyId());

        boolean haveAccess = myAccount.getRole() == Account.Role.ROLE_OWNER
                || changeAccount.getRole() == Account.Role.ROLE_USER;

        if (!existAccountByCompany || !haveAccess) {
            throw new SecurityException("Нет доступа для изменения данного пользователя");
        }
    }

    @Transactional
    public void checkSecurityForAccounts(List<Account> changeAccounts, Account myAccount) {
        List<Account> usersByAdmin = getAccountsByCompanyId(myAccount.getCompanyId());
        if (!usersByAdmin.containsAll(changeAccounts)) {
            throw new SecurityException("Нет доступа для изменения данных пользователей");
        }
    }
}
