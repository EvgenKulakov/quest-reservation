package ru.questsfera.questreservation.service.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.SlotFactory;
import ru.questsfera.questreservation.redis.object.AccountRedis;
import ru.questsfera.questreservation.redis.service.AccountRedisService;
import ru.questsfera.questreservation.service.quest.QuestService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class ReservationGetOperator {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private QuestService questService;
    @Autowired
    private AccountRedisService accountRedisService;


    @Transactional
    public Map<String, List<Slot>> getQuestsAndSlots(LocalDate date, Principal principal) {

        Map<String, List<Slot>> questsAndSlots = new LinkedHashMap<>();

        AccountRedis accountRedis = accountRedisService.findByEmailLogin(principal.getName());
        Set<Quest> quests = new TreeSet<>(questService.findAllByAccountId(accountRedis.getId()));

        for (Quest quest : quests) {
            Map<LocalTime, Reservation> reservations = reservationService.findActiveByQuestIdAndDate(quest.getId(), date);
            SlotList slotList = SlotListMapper.createObject(quest.getSlotList());
            SlotFactory slotFactory = new SlotFactory(quest, date, slotList, reservations);
            List<Slot> slots = slotFactory.getActualSlots();
            questsAndSlots.put(quest.getQuestName(), slots);
        }

        return questsAndSlots;
    }
}
