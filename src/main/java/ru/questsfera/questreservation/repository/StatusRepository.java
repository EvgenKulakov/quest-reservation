package ru.questsfera.questreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.entity.Status;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

    @Query("SELECT st FROM Status st JOIN st.quests qu WHERE qu.id = :questId")
    List<Status> findAllByQuestId(@Param("questId") Integer questId);
}