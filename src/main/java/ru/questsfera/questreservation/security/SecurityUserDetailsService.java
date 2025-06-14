package ru.questsfera.questreservation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.service.account.AccountService;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final AccountService accountService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService.getAccountByLogin(username);
        return new org.springframework.security.core.userdetails.User(
                account.getLogin(),
                account.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(account.getRole().name()))
        );
    }
}
