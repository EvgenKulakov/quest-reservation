package ru.questsfera.questreservation.cache.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
@Transactional
public class RedisRepository {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void saveWithTTL(String key, String value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public void saveDictionary(String nameDict, Map<String, String> dict, Date dateOfDeletion) {
        redisTemplate.opsForHash().putAll(nameDict, dict);
        redisTemplate.expireAt(nameDict, dateOfDeletion);
    }

    public String findByCacheId(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
