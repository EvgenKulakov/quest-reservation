package ru.questsfera.questreservation.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.cache.object.AccountCache;
import ru.questsfera.questreservation.cache.repository.AccountCacheRepository;

@Service
public class AccountCacheService {
    @Autowired
    private AccountCacheRepository accountCacheRepository;


    public void save(AccountCache accountCache) {
        accountCacheRepository.save(accountCache);
    }

    public AccountCache findByEmailLogin(String emailLogin) {
        return accountCacheRepository.findById(emailLogin).orElse(null);
    }
}
