package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.questsfera.questreservation.cache.object.ReservationCache;
import ru.questsfera.questreservation.cache.service.ReservationCacheService;
import ru.questsfera.questreservation.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/reserve")
public class ReservationRestController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationCacheService reservationCacheService;


    @GetMapping("/{questId}/{d}-{m}-{y}/{h}-{min}")
    public ReservationCache getQuestCache(@PathVariable("questId") Integer questId,
                                     @PathVariable("d") Integer d,
                                     @PathVariable("m") Integer m,
                                     @PathVariable("y") Integer y,
                                     @PathVariable("h") Integer h,
                                     @PathVariable("min") Integer min
    ) {
        ReservationCache reservationCache = reservationCacheService
                .findByQuestIdDateTime(questId, LocalDate.of(y, m, d), LocalTime.of(h, min));

        return reservationCache;
    }
}
