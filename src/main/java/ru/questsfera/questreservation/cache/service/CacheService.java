package ru.questsfera.questreservation.cache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.cache.object.Cache;
import ru.questsfera.questreservation.cache.repository.CacheRepository;

@Service
public class CacheService {

    @Autowired
    private CacheRepository cacheRepository;

    public void save(Cache cacheObject) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String cacheJSON = null;
        try {
            cacheJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cacheObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        cacheRepository.save(cacheObject.getCacheId(), cacheJSON);
    }

    public Cache findByCacheId(String cacheId, Class<? extends Cache> valueType) {
        String cacheJSON = cacheRepository.findByCacheId(cacheId);
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

