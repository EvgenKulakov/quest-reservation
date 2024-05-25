package ru.questsfera.questreservation.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.redis.object.AccountRedis;
import ru.questsfera.questreservation.redis.repository.AccountRedisRepository;

@Service
public class AccountRedisService {
    @Autowired
    private AccountRedisRepository accountRedisRepository;


    public void save(AccountRedis accountRedis) {
        accountRedisRepository.save(accountRedis);
    }

    public AccountRedis findByEmailLogin(String emailLogin) {
        return accountRedisRepository.findById(emailLogin).orElse(null);
    }

    public boolean existByEmailLogin(String emailLogin) {
        return accountRedisRepository.existsById(emailLogin);
    }

    public void deleteByEmailLogin(String emailLogin) {
        accountRedisRepository.deleteById(emailLogin);
    }
}
