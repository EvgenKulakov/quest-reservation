package ru.questsfera.questreservation.cache.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class AccountCache implements Cache {

    private Integer id;
    private String emailLogin;
    private String firstName;
    private String lastName;
    private String phone;
    private Account.Role role;
    private Integer companyId;
    private Set<Integer> questIds;

    public AccountCache(Account account) {
        this.id = account.getId();
        this.emailLogin = account.getEmailLogin();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.phone = account.getPhone();
        this.role = account.getRole();
        this.companyId = account.getCompany().getId();
        this.questIds = account.getQuests().stream().map(Quest::getId).collect(Collectors.toSet());
    }
}
