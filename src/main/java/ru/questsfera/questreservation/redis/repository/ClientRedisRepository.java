package ru.questsfera.questreservation.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.redis.object.ClientRedis;

@Repository
public interface ClientRedisRepository extends CrudRepository<ClientRedis, Integer> {
}
