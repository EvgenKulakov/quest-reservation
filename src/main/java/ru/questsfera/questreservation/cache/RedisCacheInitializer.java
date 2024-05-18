package ru.questsfera.questreservation.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.cache.object.AccountCache;
import ru.questsfera.questreservation.cache.object.QuestCache;
import ru.questsfera.questreservation.cache.object.ReservationCache;
import ru.questsfera.questreservation.cache.repository.RedisRepository;
import ru.questsfera.questreservation.cache.service.RedisService;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.service.AccountService;
import ru.questsfera.questreservation.service.QuestService;
import ru.questsfera.questreservation.service.ReservationService;

import java.time.*;
import java.time.format.DateTimeFormatter;
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
    private RedisService redisService;
    @Autowired
    private RedisRepository redisRepository;
    //TODO: logger


    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<Account> accounts = accountService.findAll();
        accounts.forEach(account -> redisService.save(new AccountCache(account)));

        List<Quest> allQuests = questService.findAll();
        List<LocalDate> dates = getDatesForCache();

        for (Quest quest : allQuests) {
            redisService.save(new QuestCache(quest));

            //TODO: не отменённые
            Map<LocalDate, List<Reservation>> reservesByDates = reservationService.findAllByQuestAndDates(quest, dates);
            for (Map.Entry<LocalDate, List<Reservation>> pair : reservesByDates.entrySet()) {
                String redisKey = String.format("reservations:[quest:%d][date:%s]",
                        quest.getId(), pair.getKey().format(DateTimeFormatter.ofPattern("dd-MM")));

                Map<String, String> mapValue = new HashMap<>();

                for (Reservation res : pair.getValue()) {
                    ReservationCache reservationCache = new ReservationCache(res);
                    String reservationJSON = null;
                    try {
                        reservationJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(reservationCache);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    String key = res.getTimeReserve().format(DateTimeFormatter.ofPattern("HH-mm"));
                    mapValue.put(key, reservationJSON);
                }

                LocalDateTime dateTimeOfDeletion = pair.getKey().plusDays(8).atStartOfDay();
                Date dateOfDeletion = Date.from(dateTimeOfDeletion.atZone(ZoneId.systemDefault()).toInstant());

                redisRepository.saveDictionary(redisKey, mapValue, dateOfDeletion);
            }
        }
    }

    private List<LocalDate> getDatesForCache() {
        List<LocalDate> dateList = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusWeeks(1);
        LocalDate endDate = today.plusWeeks(2);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateList.add(date);
        }

        return dateList;
    }
}
