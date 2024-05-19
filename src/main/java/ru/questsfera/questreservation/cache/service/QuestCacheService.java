package ru.questsfera.questreservation.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.cache.object.QuestCache;
import ru.questsfera.questreservation.cache.repository.RedisRepository;

@Service
public class QuestCacheService {
    @Autowired
    private RedisRepository redisRepository;

    private final String CACHE_ID_FORMAT = "quest:%d";


    public void save(QuestCache questCache) {
        String cacheId = createCacheId(questCache.getId());
        redisRepository.save(cacheId, questCache);
    }

    public QuestCache findByCacheId(String cacheId) {
        return (QuestCache) redisRepository.findByCacheId(cacheId);
    }

    public QuestCache findById(Integer id) {
        String cacheId = createCacheId(id);
        return (QuestCache) redisRepository.findByCacheId(cacheId);
    }

    private String createCacheId(Integer id) {
        return String.format(CACHE_ID_FORMAT, id);
    }
}
