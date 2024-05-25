package ru.questsfera.questreservation.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.redis.object.ClientRedis;
import ru.questsfera.questreservation.redis.repository.ClientRedisRepository;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.RedisCalendar;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClientRedisService {
    @Autowired
    private ClientRedisRepository clientRedisRepository;


    public void save(ClientRedis clientRedis, List<Reservation> reservations) {
        Optional<LocalDate> latestDateForCache = RedisCalendar.getLatestDateForRedis(reservations);
        if (latestDateForCache.isPresent()) {
            long timeToLive = RedisCalendar.getTimeToLive(latestDateForCache.get());
            clientRedis.setTimeToLive(timeToLive);
            clientRedisRepository.save(clientRedis);
        }
        else deleteById(clientRedis.getId());
    }

    public ClientRedis findById(Integer id) {
        return clientRedisRepository.findById(id).orElse(null);
    }

    public boolean existById(Integer id) {
        return clientRedisRepository.existsById(id);
    }

    public void deleteById(Integer clientId) {
        clientRedisRepository.deleteById(clientId);
    }
}
