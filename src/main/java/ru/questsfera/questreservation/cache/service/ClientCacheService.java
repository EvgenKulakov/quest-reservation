package ru.questsfera.questreservation.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.cache.object.ClientCache;
import ru.questsfera.questreservation.cache.repository.RedisRepository;

import java.util.Date;

@Service
public class ClientCacheService {
    @Autowired
    private RedisRepository redisRepository;

    private final String CACHE_ID_FORMAT = "client:%d";


    public void save(ClientCache clientCache, Date dateOfDeletion) {
        String cacheId = createCacheId(clientCache.getId());
        redisRepository.saveWithExpire(cacheId, clientCache, dateOfDeletion);
    }

    public ClientCache findByCacheId(String cacheId) {
        return (ClientCache) redisRepository.findByCacheId(cacheId);
    }

    public ClientCache findById(Integer id) {
        String cacheId = createCacheId(id);
        return (ClientCache) redisRepository.findByCacheId(cacheId);
    }

    public boolean existById(Integer id) {
        String cacheId = createCacheId(id);
        return redisRepository.existByCacheId(cacheId);
    }

    private String createCacheId(Integer id) {
        return String.format(CACHE_ID_FORMAT, id);
    }
}
