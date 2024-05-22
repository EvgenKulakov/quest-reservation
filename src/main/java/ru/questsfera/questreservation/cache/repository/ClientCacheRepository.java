package ru.questsfera.questreservation.cache.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.cache.object.ClientCache;

@Repository
public interface ClientCacheRepository extends CrudRepository<ClientCache, Integer> {
}
