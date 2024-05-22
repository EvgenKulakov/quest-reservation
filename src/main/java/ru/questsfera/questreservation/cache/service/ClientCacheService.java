package ru.questsfera.questreservation.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.cache.object.ClientCache;
import ru.questsfera.questreservation.cache.repository.ClientCacheRepository;

@Service
public class ClientCacheService {
    @Autowired
    private ClientCacheRepository clientCacheRepository;


    public void save(ClientCache clientCache) {
        clientCacheRepository.save(clientCache);
    }

    public ClientCache findById(Integer id) {
        return clientCacheRepository.findById(id).orElse(null);
    }

    public boolean existById(Integer id) {
        return clientCacheRepository.existsById(id);
    }
}
