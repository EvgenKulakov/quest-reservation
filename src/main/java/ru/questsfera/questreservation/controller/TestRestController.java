package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.redis.object.ReservationRedis;
import ru.questsfera.questreservation.redis.service.ReservationRedisService;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.reservation.ReservationService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/reserve")
public class TestRestController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRedisService reservationRedisService;
    @Autowired
    private AccountService accountService;


    @GetMapping("/{questId}/{d}-{m}-{y}/{h}-{min}")
    public ReservationRedis getQuestCache(@PathVariable("questId") Integer questId,
                                          @PathVariable("d") Integer d,
                                          @PathVariable("m") Integer m,
                                          @PathVariable("y") Integer y,
                                          @PathVariable("h") Integer h,
                                          @PathVariable("min") Integer min
    ) {
        ReservationRedis reservationRedis = reservationRedisService
                .findByQuestIdDateTime(questId, LocalDate.of(y, m, d), LocalTime.of(h, min));

        return reservationRedis;
    }

    @GetMapping("get-acc")
    public Account getAccount(Principal principal) {
        String accName = principal.getName();
        Account account = accountService.getAccountByLogin(accName);
        return account;
    }

    @GetMapping("upd-acc")
    public Account updateAcc(Principal principal) {
        String accName = principal.getName();
        Account account = accountService.getAccountByLogin(accName);
        account.setFirstName("Egor " + Math.random());
        accountService.saveAccount(account);
        return account;
    }

    @GetMapping("del-acc")
    public Account delAcc(Principal principal) {
        String accName = principal.getName();
        Account account = accountService.getAccountByLogin(accName);
        accountService.delete(account);
        return account;
    }
}
