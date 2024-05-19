package ru.questsfera.questreservation.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.cache.object.ReservationCache;
import ru.questsfera.questreservation.cache.repository.RedisRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class ReservationCacheService {
    @Autowired
    private RedisRepository redisRepository;

    private final String CACHE_ID_FORMAT = "reserve:[quest:%d][datetime:%s-%s]";


    public void save(ReservationCache cache, Date dateOfDeletion) {
        String cacheId = createCacheId(cache.getQuestId(), cache.getDateReserve(), cache.getTimeReserve());
        redisRepository.saveWithExpire(cacheId, cache, dateOfDeletion);
    }

    public ReservationCache findByCacheId(String cacheId) {
        return (ReservationCache) redisRepository.findByCacheId(cacheId);
    }

    public ReservationCache findByQuestIdDateTime(Integer questId, LocalDate dateReserve, LocalTime timeReserve) {
        String cacheId = createCacheId(questId, dateReserve, timeReserve);
        return (ReservationCache) redisRepository.findByCacheId(cacheId);
    }

    private String createCacheId(Integer questId, LocalDate dateReserve, LocalTime timeReserve) {
        return String.format(CACHE_ID_FORMAT,
                questId,
                dateReserve.format(DateTimeFormatter.ofPattern("dd-MM")),
                timeReserve.format(DateTimeFormatter.ofPattern("HH-mm")));
    }
}
