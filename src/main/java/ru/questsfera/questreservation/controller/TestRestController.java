package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.questsfera.questreservation.cache.object.ReservationCache;
import ru.questsfera.questreservation.cache.repository.ReservationCacheRepository;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.service.AccountService;
import ru.questsfera.questreservation.service.ReservationService;

import java.time.LocalTime;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class TestRestController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ReservationCacheRepository reservationCacheRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @GetMapping("/account/{username}")
    public Account showAdminByName(@PathVariable("username") String username) {
        Account account = accountService.getAccountByLogin(username);
        return account;
    }

    @GetMapping("/redis")
    public String testRedis() {

//        redisTemplate.opsForValue().set("myKey", "myValue");
//        String value = redisTemplate.opsForValue().get("myKey");
//        System.out.println(value);

//        ReservationCache reservationCache = new ReservationCache(
//                "2",
//                "vadim",
//                "ebanomv"
//        );

//        reservationCacheRepository.save(reservationCache);

        return "success";
    }

    @GetMapping("/slot/{id}")
    public Slot getSlot(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.getReserveById(id);
        Slot slot = null;
        if (reservation != null) {
            slot = new Slot(reservation.getQuest(), reservation.getStatusType(), reservation,
                    reservation.getDateReserve(), reservation.getTimeReserve(), 3000, LocalTime.MIN);
        } else {
            slot = new Slot();
        }
        return slot;
    }

    @GetMapping("/reservation/{id}")
    public Reservation getReservation(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.getReserveById(id);
        return reservation;
    }
}
