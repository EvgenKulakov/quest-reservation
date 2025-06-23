package ru.questsfera.questreservation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.mapper.AccountMapper;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.service.account.AccountService;

@Service
@RequiredArgsConstructor
public class SecurityAccountService implements UserDetailsService {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService.getAccountByLogin(username);
        return accountMapper.toAccountUserDetails(account);
    }
}
