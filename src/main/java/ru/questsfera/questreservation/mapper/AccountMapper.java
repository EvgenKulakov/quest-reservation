package ru.questsfera.questreservation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.questsfera.questreservation.model.dto.AccountCreateForm;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.security.AccountUserDetails;

import java.util.Collection;
import java.util.Collections;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "quests", ignore = true)
    Account toEntity(AccountCreateForm accountCreateForm, Integer companyId);

    @Mapping(source = "login", target = "username")
    @Mapping(source = "role", target = "authorities", qualifiedByName = "roleToAuthorities")
    AccountUserDetails toAccountUserDetails(Account account);

    @Named("roleToAuthorities")
    default Collection<GrantedAuthority> roleToAuthorities(Account.Role role) {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }
}
