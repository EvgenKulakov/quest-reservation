package ru.questsfera.questreservation.cache;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.cache.object.AccountCache;
import ru.questsfera.questreservation.cache.object.ClientCache;
import ru.questsfera.questreservation.cache.object.QuestCache;
import ru.questsfera.questreservation.cache.object.ReservationCache;
import ru.questsfera.questreservation.cache.service.AccountCacheService;
import ru.questsfera.questreservation.cache.service.ClientCacheService;
import ru.questsfera.questreservation.cache.service.QuestCacheService;
import ru.questsfera.questreservation.cache.service.ReservationCacheService;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.CacheCalendar;
import ru.questsfera.questreservation.service.AccountService;
import ru.questsfera.questreservation.service.QuestService;
import ru.questsfera.questreservation.service.ReservationService;

import java.time.*;
import java.util.*;

@Component
public class RedisCacheInitializer {
    @Autowired
    private AccountService accountService;
    @Autowired
    private QuestService questService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private AccountCacheService accountCacheService;
    @Autowired
    private QuestCacheService questCacheService;
    @Autowired
    private ReservationCacheService reservationCacheService;
    @Autowired
    private ClientCacheService clientCacheService;

    private Logger logger = LoggerFactory.getLogger(RedisCacheInitializer.class);


    @PostConstruct
    public void init() {
        LocalTime startInit = LocalTime.now();

        List<Account> accounts = accountService.findAll();
        accounts.forEach(account -> accountCacheService.save(new AccountCache(account)));

        List<Quest> quests = questService.findAll();
        quests.forEach(quest -> questCacheService.save(new QuestCache(quest)));

        List<LocalDate> dates = CacheCalendar.getDatesForCache();
        List<Reservation> reservationsByDates = reservationService.findActiveByDates(dates);

        for (Reservation reservation : reservationsByDates) {
            Date dateOfDeletion = CacheCalendar.getDateOfDeletion(reservation.getDateReserve());
            ReservationCache reservationCache = new ReservationCache(reservation);
            reservationCacheService.save(reservationCache, dateOfDeletion);

            if (reservation.getClient() != null) {
                ClientCache clientCache = new ClientCache(reservation.getClient());
                if (clientCache.getReservationIds().size() > 1) {
                    if (!clientCacheService.existById(clientCache.getId())) {
                        LocalDate latestDate = reservationService
                                .findAllByListId(clientCache.getReservationIds())
                                .stream()
                                .filter(r -> r.getStatusType() != StatusType.CANCEL)
                                .map(Reservation::getDateReserve)
                                .max(Comparator.naturalOrder())
                                .orElseThrow();
                        clientCacheService.save(clientCache, CacheCalendar.getDateOfDeletion(latestDate));
                    }
                } else {
                    clientCacheService.save(clientCache, dateOfDeletion);
                }
            }
        }

        Duration duration = Duration.between(startInit, LocalTime.now());
        long seconds = duration.getSeconds();
        long milliseconds = duration.toMillis() % 1000;

        logger.info(String.format("Cache was created. Spent time: seconds:%d milliseconds:%d", seconds, milliseconds));
    }
}
