package ru.questsfera.questreservation.cache.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.entity.Account;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class AccountCache implements Cache {

    private String cacheId;
    private String emailLogin;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Account.Role role;
    private String companyId;
    private Set<String> questIds;

    public AccountCache(Account account) {
        this.cacheId = "account:%d".formatted(account.getId());
        this.emailLogin = account.getEmailLogin();
        this.password = account.getPassword();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.phone = account.getPhone();
        this.role = account.getRole();
        this.companyId = "company:%d".formatted(account.getCompany().getId());
        this.questIds = account.getQuests()
                .stream()
                .map(quest -> "quest:%d".formatted(quest.getId()))
                .collect(Collectors.toSet());
    }
}
