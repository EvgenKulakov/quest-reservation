package ru.questsfera.questreservation.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.redis.object.AccountRedis;

@Repository
public interface AccountRedisRepository extends CrudRepository<AccountRedis, String> {
}
