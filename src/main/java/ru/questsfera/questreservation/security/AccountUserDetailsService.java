package ru.questsfera.questreservation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.mapper.AccountMapper;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.repository.jpa.AccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findAccountByLoginWithQuests(username);
        if (accountOptional.isPresent()) {
            return accountMapper.toAccountUserDetails(accountOptional.get());
        }
        throw new UsernameNotFoundException(String.format("Пользователь %s не найден", username));
    }

    public void updateAccountUserDetails(AccountUserDetails accountUserDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                accountUserDetails,
                auth.getCredentials(),
                accountUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
