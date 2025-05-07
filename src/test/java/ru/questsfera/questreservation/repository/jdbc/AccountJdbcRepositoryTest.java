package ru.questsfera.questreservation.repository.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.questsfera.questreservation.model.entity.Account;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(AccountJdbcRepository.class)
@Sql(scripts = {"classpath:common_test_data.sql"})
class AccountJdbcRepositoryTest {

    static final String ACCOUNT_ROLE_OWNER_LOGIN = "admin@gmail.com";
    static final String ACCOUNT_ROLE_ADMIN_LOGIN = "second@gmail.com";
    static final String ACCOUNT_ROLE_USER_LOGIN = "third@gmail.com";
    static final String NOT_EXISTS_LOGIN = "not-exists-login@gmail.com";

    @Autowired
    AccountJdbcRepository accountJdbcRepository;

    @Test
    void findOwnAccountsByMyAccountOrderByName_success() {
        List<Account> actualAccByOwner = accountJdbcRepository.findOwnAccountsByMyAccountOrderByName(getAccountOwner());
        List<Account> exceptedAccByOwner = List.of(getAccountAdmin(), getAccountUser());
        assertThat(actualAccByOwner)
                .usingRecursiveComparison()
                .isEqualTo(exceptedAccByOwner);

        List<Account> actualAccByAdmin = accountJdbcRepository.findOwnAccountsByMyAccountOrderByName(getAccountAdmin());
        List<Account> exceptedAccByAdmin = List.of(getAccountUser());
        assertThat(actualAccByAdmin)
                .usingRecursiveComparison()
                .isEqualTo(exceptedAccByAdmin);
    }

    @Test
    void findOwnAccountsByMyAccountOrderByName_empty() {
        List<Account> actualAccByUser = accountJdbcRepository.findOwnAccountsByMyAccountOrderByName(getAccountUser());
        assertThat(actualAccByUser.isEmpty()).isTrue();
    }

    @Test
    void findAllAccountsInCompanyByOwnAccountName_success() {
        List<Account> actualAccByExistsLogin =
                accountJdbcRepository.findAllAccountsInCompanyByOwnAccountName(ACCOUNT_ROLE_OWNER_LOGIN);
        List<Account> exceptedAccByExistsLogin = List.of(getAccountOwner(), getAccountAdmin(), getAccountUser());
        assertThat(actualAccByExistsLogin)
                .usingRecursiveComparison()
                .isEqualTo(exceptedAccByExistsLogin);
    }

    @Test
    void findAllAccountsInCompanyByOwnAccountName_empty() {
        List<Account> actualAccByNotExistingLogin =
                accountJdbcRepository.findAllAccountsInCompanyByOwnAccountName(NOT_EXISTS_LOGIN);
        assertThat(actualAccByNotExistingLogin.isEmpty()).isTrue();
    }

    private Account getAccountOwner() {
        return Account.builder()
                .id(1)
                .login(ACCOUNT_ROLE_OWNER_LOGIN)
                .password("$2a$10$I6WnbfYRb2Z8uBysTKy5l.uSazvJYhqFgsj4LQ.5vZc65TmGlcat6")
                .firstName("Test")
                .lastName("Евгений")
                .phone("+79995554433")
                .role(Account.Role.ROLE_OWNER)
                .companyId(1)
                .build();
    }

    private Account getAccountAdmin() {
        return Account.builder()
                .id(2)
                .login(ACCOUNT_ROLE_ADMIN_LOGIN)
                .password("$2a$10$l0VKw9rNsW.z609bimtWEOyjrVSYWf8Lskriij08nAyS1PqLNfnxq")
                .firstName("second")
                .lastName("second")
                .role(Account.Role.ROLE_ADMIN)
                .companyId(1)
                .build();
    }

    private Account getAccountUser() {
        return Account.builder()
                .id(3)
                .login(ACCOUNT_ROLE_USER_LOGIN)
                .password("$2a$10$CIkKUYln9NlUsaMS2ODTkOVR7HyoszJm/DWAIf9MFSW14HqqHDvw6")
                .firstName("second")
                .lastName("third")
                .role(Account.Role.ROLE_USER)
                .companyId(1)
                .build();
    }
}