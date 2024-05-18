package ru.questsfera.questreservation.cache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.cache.object.Cache;
import ru.questsfera.questreservation.cache.repository.RedisRepository;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisRepository redisRepository;

    public void save(Cache cacheObject) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String cacheJSON = null;
        try {
            cacheJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cacheObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        redisRepository.save(cacheObject.getCacheId(), cacheJSON);
    }

    public void saveWithTTL(Cache cacheObject, long timeout, TimeUnit timeUnit) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String cacheJSON = null;
        try {
            cacheJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cacheObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        redisRepository.saveWithTTL(cacheObject.getCacheId(), cacheJSON, timeout, timeUnit);
    }

    public Cache findByCacheId(String cacheId, Class<? extends Cache> valueType) {
        String cacheJSON = redisRepository.findByCacheId(cacheId);
        if (cacheJSON == null) return null;

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Cache cacheObject = null;
        try {
            cacheObject = mapper.readValue(cacheJSON, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return cacheObject;
    }
}

