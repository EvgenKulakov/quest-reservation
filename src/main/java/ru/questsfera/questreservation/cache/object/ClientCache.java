package ru.questsfera.questreservation.cache.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.entity.BlackList;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Reservation;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ClientCache implements Cache {

    private Integer id;
    private String firstName;
    private String lastName;
    private List<String> phoneIds;
    private List<String> emailIds;
    private BlackList blackList;
    private Integer companyId;
    private List<Long> reservationIds;

    public ClientCache(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        //TODO: phones and emails
        this.phoneIds = new ArrayList<>(List.of(client.getPhones()));
        this.emailIds = new ArrayList<>(List.of(client.getEmails()));
        //TODO: BlackListCache
        this.blackList = client.getBlackList();
        this.companyId = client.getCompany().getId();
        this.reservationIds = client.getReservations().stream().map(Reservation::getId).toList();
    }
}
