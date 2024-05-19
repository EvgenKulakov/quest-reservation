package ru.questsfera.questreservation.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.cache.object.AccountCache;
import ru.questsfera.questreservation.cache.repository.RedisRepository;

@Service
public class AccountCacheService {
    @Autowired
    private RedisRepository redisRepository;

    private final String CACHE_ID_FORMAT = "account:%s";


    public void save(AccountCache accountCache) {
        String cacheId = createCacheId(accountCache.getEmailLogin());
        redisRepository.save(cacheId, accountCache);
    }

    public AccountCache findByCacheId(String cacheId) {
        return (AccountCache) redisRepository.findByCacheId(cacheId);
    }

    public AccountCache findByLogin(String login) {
        String cacheId = createCacheId(login);
        return (AccountCache) redisRepository.findByCacheId(cacheId);
    }

    private String createCacheId(String login) {
        return String.format(CACHE_ID_FORMAT, login);
    }
}
