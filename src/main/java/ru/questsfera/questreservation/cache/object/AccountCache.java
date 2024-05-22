package ru.questsfera.questreservation.cache.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("account")
public class AccountCache {

    @Id private String emailLogin;
    private Integer id;
    private String firstName;
    private String lastName;
    private String phone;
    private Account.Role role;
    private Integer companyId;
    private Set<Integer> questIds;

    public AccountCache(Account account) {
        this.emailLogin = account.getEmailLogin();
        this.id = account.getId();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.phone = account.getPhone();
        this.role = account.getRole();
        this.companyId = account.getCompany().getId();
        this.questIds = account.getQuests().stream().map(Quest::getId).collect(Collectors.toSet());
    }
}
