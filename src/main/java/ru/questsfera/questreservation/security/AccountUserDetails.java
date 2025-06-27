package ru.questsfera.questreservation.security;

import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.questsfera.questreservation.model.entity.Account;

import java.util.Collection;
import java.util.Set;

@Value
public class AccountUserDetails implements UserDetails {

    Integer id;
    Account.Role role;
    Integer companyId;
    Set<Integer> questIds;

    String username;
    String password;
    Collection<GrantedAuthority> authorities;
}
