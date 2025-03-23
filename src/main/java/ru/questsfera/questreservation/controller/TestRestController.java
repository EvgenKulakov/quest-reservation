package ru.questsfera.questreservation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.reservation.ReservationService;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestRestController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/{questId}/{d}-{m}-{y}/{h}-{min}")
    public Reservation getQuestCache(@PathVariable("questId") Integer questId,
                                          @PathVariable("d") Integer d,
                                          @PathVariable("m") Integer m,
                                          @PathVariable("y") Integer y,
                                          @PathVariable("h") Integer h,
                                          @PathVariable("min") Integer min
    ) {
        return null;
    }

    @GetMapping("/{id}")
    public Reservation findReservation(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.getReserveById(id);
        return reservation;
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
