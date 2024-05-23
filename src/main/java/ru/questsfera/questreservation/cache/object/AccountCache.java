package ru.questsfera.questreservation.cache.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import ru.questsfera.questreservation.entity.Account;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("account")
public class AccountCache {

    @Id private String emailLogin;
    private Integer id;
    private Account.Role role;
    private Integer companyId;

    public AccountCache(Account account) {
        this.emailLogin = account.getEmailLogin();
        this.id = account.getId();
        this.role = account.getRole();
        this.companyId = account.getCompany().getId();
    }
}
