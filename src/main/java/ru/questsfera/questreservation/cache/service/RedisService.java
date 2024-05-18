package ru.questsfera.questreservation.cache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.cache.object.Cache;
import ru.questsfera.questreservation.cache.repository.RedisRepository;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private ObjectMapper mapper;


    public void save(Cache cacheObject) {

        String cacheJSON = null;
        try {
            cacheJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cacheObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        redisRepository.save(cacheObject.getCacheId(), cacheJSON);
    }

    public void saveWithTTL(Cache cacheObject, long timeout, TimeUnit timeUnit) {

        String cacheJSON = null;
        try {
            cacheJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cacheObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        redisRepository.saveWithTTL(cacheObject.getCacheId(), cacheJSON, timeout, timeUnit);
    }

    public void saveWithExpire(Cache cacheObject, Date dateOfDeletion) {

        String cacheJSON = null;
        try {
            cacheJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cacheObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        redisRepository.saveWithExpire(cacheObject.getCacheId(), cacheJSON, dateOfDeletion);
    }

    public void saveDictionary(String nameDict, Map<String, String> dict, Date dateOfDeletion) {
        redisRepository.saveDictionary(nameDict, dict, dateOfDeletion);
    }

    public Cache findByCacheId(String cacheId, Class<? extends Cache> valueType) {
        String cacheJSON = redisRepository.findByCacheId(cacheId);
        if (cacheJSON == null) return null;

        Cache cacheObject = null;
        try {
            cacheObject = mapper.readValue(cacheJSON, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return cacheObject;
    }

    public Boolean existCacheObject(String key) {
        return redisRepository.existCacheObject(key);
    }
}

