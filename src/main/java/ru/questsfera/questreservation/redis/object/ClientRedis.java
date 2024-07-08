package ru.questsfera.questreservation.redis.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import ru.questsfera.questreservation.entity.Client;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("client")
public class ClientRedis {

    @Id private Integer id;
    @TimeToLive private Long timeToLive;
    private String firstName;
    private String lastName;
    private List<String> phoneIds;
    private List<String> emailIds;
    private Integer blacklistId;
    private Integer companyId;

    public ClientRedis(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        //TODO: phones and emails
        this.phoneIds = new ArrayList<>(List.of(client.getPhones()));
        this.emailIds = new ArrayList<>(List.of(client.getEmails()));
        this.blacklistId = client.getBlacklistId();
        this.companyId = client.getCompanyId();
    }
}
