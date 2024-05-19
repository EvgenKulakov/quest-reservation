package ru.questsfera.questreservation.cache.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.cache.object.Cache;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepository {

    @Autowired
    private RedisTemplate<String, Cache> redisTemplate;


    public void save(String key, Cache value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void saveWithTTL(String key, Cache value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public void saveWithExpire(String key, Cache value, Date dateOfDeletion) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expireAt(key, dateOfDeletion);
    }

    public Cache findByCacheId(String cacheId) {
        return redisTemplate.opsForValue().get(cacheId);
    }

    public Boolean existByCacheId(String cacheId) {
        return redisTemplate.hasKey(cacheId);
    }
}
