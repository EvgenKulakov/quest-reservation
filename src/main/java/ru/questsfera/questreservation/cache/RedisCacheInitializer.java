package ru.questsfera.questreservation.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.cache.object.AccountCache;
import ru.questsfera.questreservation.cache.object.ClientCache;
import ru.questsfera.questreservation.cache.object.QuestCache;
import ru.questsfera.questreservation.cache.object.ReservationCache;
import ru.questsfera.questreservation.cache.service.RedisService;
import ru.questsfera.questreservation.dto.StatusType;
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
    private ObjectMapper mapper;

    private Logger logger = LoggerFactory.getLogger(RedisCacheInitializer.class);


    @PostConstruct
    public void init() {
        List<Account> accounts = accountService.findAll();
        accounts.forEach(account -> redisService.save(new AccountCache(account)));

        List<Quest> allQuests = questService.findAll();
        List<LocalDate> dates = getDatesForCache();

        for (Quest quest : allQuests) {
            redisService.save(new QuestCache(quest));

            Map<LocalDate, List<Reservation>> reservesByDates = reservationService.findActiveByQuestAndDates(quest, dates);
            for (Map.Entry<LocalDate, List<Reservation>> pair : reservesByDates.entrySet()) {
                String redisKey = String.format("reservations:[quest:%d][date:%s]",
                        quest.getId(), pair.getKey().format(DateTimeFormatter.ofPattern("dd-MM")));

                Date dateOfDeletion = getDateOfDeletion(pair.getKey());

                Map<String, String> mapValue = new HashMap<>();

                for (Reservation res : pair.getValue()) {
                    if (res.getClient() != null) {
                        ClientCache clientCache = new ClientCache(res.getClient());
                        if (clientCache.getReservationIds().size() > 1) {
                            if (!redisService.existCacheObject(clientCache.getCacheId())) {
                                LocalDate latestDate = reservationService
                                        .findAllByListId(clientCache.getReservationIds())
                                        .stream()
                                        .filter(r -> r.getStatusType() != StatusType.CANCEL)
                                        .map(Reservation::getDateReserve)
                                        .max(Comparator.naturalOrder())
                                        .orElseThrow();
                                redisService.saveWithExpire(clientCache, getDateOfDeletion(latestDate));
                            }
                        } else {
                            redisService.saveWithExpire(clientCache, dateOfDeletion);
                        }
                    }

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

                redisService.saveDictionary(redisKey, mapValue, dateOfDeletion);
            }
        }
        logger.info("Cache was created");
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

    private Date getDateOfDeletion(LocalDate localDate) {
        LocalDateTime dateTimeOfDeletion = localDate.plusDays(8).atStartOfDay();
        Date dateOfDeletion = Date.from(dateTimeOfDeletion.atZone(ZoneId.systemDefault()).toInstant());
        return dateOfDeletion;
    }
}
