package ru.questsfera.questreservation.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.questsfera.questreservation.model.dto.AccountCreateForm;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.security.AccountUserDetails;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class AccountMapperTest {

    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Test
    void toEntity_success() {
        AccountCreateForm accountCreateForm = getAccountCreateForm();
        Integer companyId = 1;
        Account actual = accountMapper.toEntity(accountCreateForm, companyId);
        Account excepted = getAccountWithoutId();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(excepted);
    }

    @Test
    void toEntity_returnNull() {
        Account actual = accountMapper.toEntity(null, null);
        assertThat(actual).isNull();
    }

    @Test
    void toAccountUserDetails_success() {
        Account account = getAccount();
        AccountUserDetails actual = accountMapper.toAccountUserDetails(account);
        AccountUserDetails excepted = getAccountUserDetails();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(excepted);
    }

    @Test
    void toAccountUserDetails_returnNull() {
        AccountUserDetails actual = accountMapper.toAccountUserDetails(null);
        assertThat(actual).isNull();
    }

    @Test
    void roleToAuthorities_success() {
        Account.Role role = Account.Role.ROLE_OWNER;
        Collection<GrantedAuthority> actual = accountMapper.roleToAuthorities(role);
        Collection<GrantedAuthority> excepted = Collections.singleton(new SimpleGrantedAuthority(role.name()));

        assertThat(actual).isEqualTo(excepted);
    }

    private Account getAccount() {
        return Account.builder()
                .id(1)
                .login("login")
                .password("password")
                .firstName("Test")
                .lastName("Ivan")
                .role(Account.Role.ROLE_OWNER)
                .companyId(1)
                .build();
    }

    private Account getAccountWithoutId() {
        return Account.builder()
                .login("login")
                .password("password")
                .firstName("Test")
                .lastName("Ivan")
                .role(Account.Role.ROLE_OWNER)
                .companyId(1)
                .build();
    }

    private AccountCreateForm getAccountCreateForm() {
        AccountCreateForm accountCreateForm = new AccountCreateForm();

        accountCreateForm.setLogin("login");
        accountCreateForm.setPassword("password");
        accountCreateForm.setFirstName("Test");
        accountCreateForm.setLastName("Ivan");
        accountCreateForm.setRole(Account.Role.ROLE_OWNER);
        accountCreateForm.setCompanyName("Some Company");

        return accountCreateForm;
    }

    private AccountUserDetails getAccountUserDetails() {
        return new AccountUserDetails(
                1,
                Account.Role.ROLE_OWNER,
                1,
                "login",
                "password",
                Collections.singleton(new SimpleGrantedAuthority(Account.Role.ROLE_OWNER.name()))
        );
    }
}