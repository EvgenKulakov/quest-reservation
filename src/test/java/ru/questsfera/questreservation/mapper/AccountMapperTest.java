package ru.questsfera.questreservation.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.questsfera.questreservation.model.dto.AccountCreateForm;
import ru.questsfera.questreservation.model.entity.Account;

import static org.assertj.core.api.Assertions.assertThat;

class AccountMapperTest {

    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Test
    void toEntity_success() {
        AccountCreateForm accountCreateForm = getAccountCreateForm();
        Integer companyId = 1;
        Account actual = accountMapper.toEntity(accountCreateForm, companyId);
        Account excepted = getAccount();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(excepted);
    }

    @Test
    void toEntity_returnNull() {
        Account actual = accountMapper.toEntity(null, null);
        assertThat(actual).isNull();
    }

    private Account getAccount() {
        return Account.builder()
                .login("admin@gmail.com")
                .password("$2a$10$I6WnbfYRb2Z8uBysTKy5l.uSazvJYhqFgsj4LQ.5vZc65TmGlcat6")
                .firstName("Test")
                .lastName("Ivan")
                .role(Account.Role.ROLE_OWNER)
                .companyId(1)
                .build();
    }

    private AccountCreateForm getAccountCreateForm() {
        AccountCreateForm accountCreateForm = new AccountCreateForm();

        accountCreateForm.setLogin("admin@gmail.com");
        accountCreateForm.setPassword("$2a$10$I6WnbfYRb2Z8uBysTKy5l.uSazvJYhqFgsj4LQ.5vZc65TmGlcat6");
        accountCreateForm.setFirstName("Test");
        accountCreateForm.setLastName("Ivan");
        accountCreateForm.setRole(Account.Role.ROLE_OWNER);
        accountCreateForm.setCompanyName("Some Company");

        return accountCreateForm;
    }
}