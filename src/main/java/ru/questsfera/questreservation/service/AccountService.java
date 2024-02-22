package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.repository.AccountRepository;
import ru.questsfera.questreservation.repository.QuestRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

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
    public List<Account> getAccountsByCompany(Company company) {
        return accountRepository.findAllByCompanyOrderByEmailLogin(company);
    }

    @Transactional
    public List<Account> getAccountsByQuest(Quest quest) {
        return accountRepository.findAllByQuestId(quest.getId());
    }

    @Transactional
    public boolean existAccountByLogin(String emailLogin) {
        if (emailLogin.isEmpty()) return false;
        return accountRepository.existsAccountByEmailLogin(emailLogin);
    }

    @Transactional
    public boolean existAccountByCompany(Account account, Company company) {
        return accountRepository.existsAccountByIdAndCompany(account.getId(), company);
    }

    @Transactional
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(Account account) {
//        checkSecurityForUser(user, admin);
        accountRepository.delete(account);
    }

    @Transactional
    public void checkSecurityForAccount(Account account, Company company) {
        boolean existUserByAdmin = existAccountByCompany(account, company);
        if (!existUserByAdmin) {
            throw new SecurityException("Нет доступа для изменения данного пользователя");
        }
    }
}
