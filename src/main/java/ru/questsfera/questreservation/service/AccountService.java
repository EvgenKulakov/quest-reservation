package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.dto.Account;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.User;
import ru.questsfera.questreservation.repository.AdminRepository;
import ru.questsfera.questreservation.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = getAccountByName(username);
        return new org.springframework.security.core.userdetails.User(
                account.getUsername(),
                account.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(account.getRole().name()))
        );
    }

    @Transactional
    public Account getAccountByName(String username) {

        Optional<Admin> adminOptional = adminRepository.findAdminByUsername(username);
        if (adminOptional.isPresent()) {
            return adminOptional.get();
        }

        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }

        throw new UsernameNotFoundException(String.format("Пользователь %s не найден", username));
    }
}
