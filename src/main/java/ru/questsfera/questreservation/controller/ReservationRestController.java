package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.questsfera.questreservation.redis.object.ReservationRedis;
import ru.questsfera.questreservation.redis.service.ReservationRedisService;
import ru.questsfera.questreservation.service.reservation.ReservationService;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/reserve")
public class ReservationRestController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRedisService reservationRedisService;


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
}
