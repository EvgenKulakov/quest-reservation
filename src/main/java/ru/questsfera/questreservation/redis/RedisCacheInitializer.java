package ru.questsfera.questreservation.redis;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.redis.object.AccountRedis;
import ru.questsfera.questreservation.redis.service.AccountRedisService;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.service.account.AccountService;

import java.time.*;
import java.util.*;

@Slf4j
@Component
public class RedisCacheInitializer {
    @Autowired
    private AccountService accountService;
//    @Autowired
//    private ReservationService reservationService;
    @Autowired
    private AccountRedisService accountRedisService;
//    @Autowired
//    private ReservationRedisService reservationRedisService;
//    @Autowired
//    private ClientRedisService clientRedisService;
    @Autowired
    private DefaultStringRedisConnection redisConnection;


    @PostConstruct
    public void init() {
        LocalTime startInit = LocalTime.now();

        redisConnection.flushAll();

        List<Account> accounts = accountService.findAll();
        accounts.forEach(account -> accountRedisService.save(new AccountRedis(account)));

//        List<LocalDate> dates = RedisCalendar.getDatesForRedis();
//        List<Reservation> reservationsByDates = reservationService.findActiveByDates(dates);
//        addReservationsAndClientsToCache(reservationsByDates);

        Duration duration = Duration.between(startInit, LocalTime.now());
        long seconds = duration.getSeconds();
        long milliseconds = duration.toMillis() % 1000;

        log.info(String.format("Cache was created. Spent time: seconds:%d milliseconds:%d", seconds, milliseconds));
    }

//    @Scheduled(cron = "0 0 0 * * ?")
//    public void initNewDay() {
//        LocalDate lastDateForCache = LocalDate.now().plusWeeks(2);
//        List<Reservation> reservationsByDate = reservationService.findActiveByDate(lastDateForCache);
//        addReservationsAndClientsToCache(reservationsByDate);
//        log.info("Added new day in cache");
//    }

//    private void addReservationsAndClientsToCache(List<Reservation> reservations) {
//
//        for (Reservation reservation : reservations) {
//            ReservationRedis reservationRedis = new ReservationRedis(reservation);
//            reservationRedisService.save(reservationRedis);
//
//            if (reservation.getClient() != null) {
//                Client client = reservation.getClient();
//                ClientRedis clientRedis = new ClientRedis(client);
//                List<Reservation> reserveByClient = reservationService.findActiveByClientId(client.getId());
//                clientRedisService.save(clientRedis, reserveByClient);
//            }
//        }
//    }
}
