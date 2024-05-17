package ru.questsfera.questreservation.cache.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.entity.Client;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ClientCache implements Cache {

    private String cacheId;
    private String firstName;
    private String lastName;
    private List<String> phoneIds;
    private List<String> emailIds;
    private String blackListId;
    private String companyId;
    private List<String> reservationIds;

    public ClientCache(Client client) {
        this.cacheId = "client:%d".formatted(client.getId());
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        //TODO: phones and emails
        this.phoneIds = new ArrayList<>(List.of(client.getPhones()));
        this.emailIds = new ArrayList<>(List.of(client.getEmails()));
        this.blackListId = "blacklist:%d".formatted(client.getBlackList().getId());
        this.companyId = "company:%d".formatted(client.getCompany().getId());
        this.reservationIds = client.getReservations()
                .stream()
                .map(reservation -> "reservation:%d".formatted(reservation.getId()))
                .toList();
    }
}
