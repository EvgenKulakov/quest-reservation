package ru.questsfera.questreservation.cache.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.cache.object.ReservationCache;

@Repository
public class ReservationCacheRepository {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void save(ReservationCache reservationCache) {
        redisTemplate.opsForValue().set("idtest", "test");
    }
}
