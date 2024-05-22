package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.cache.object.ClientCache;
import ru.questsfera.questreservation.cache.service.ClientCacheService;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.processor.CacheCalendar;
import ru.questsfera.questreservation.repository.ClientRepository;

import java.time.LocalDate;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientCacheService clientCacheService;

    @Transactional()
    public void save(Client client) {
        clientRepository.save(client);

        ClientCache clientCache = new ClientCache(client);

        if (clientCache.getReservationIds().size() > 1) {
            LocalDate latestDate = CacheCalendar.getLatestDateReservation(client.getReservations());
            long timeToLive = CacheCalendar.getTimeToLive(latestDate);
            clientCache.setTimeToLive(timeToLive);
            clientCacheService.save(clientCache);
        } else if (clientCache.getReservationIds().size() == 1) {
            LocalDate dateReserve = client.getReservations().get(0).getDateReserve();
            long timeToLive = CacheCalendar.getTimeToLive(dateReserve);
            clientCache.setTimeToLive(timeToLive);
            clientCacheService.save(clientCache);
        }
    }
}
