package ru.questsfera.questreservation.service.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.dto.Slot;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.dto.SlotListPageDTO;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.SlotFactory;
import ru.questsfera.questreservation.service.account.AccountService;
import ru.questsfera.questreservation.service.quest.QuestService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationGetOperator {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private QuestService questService;
    @Autowired
    private AccountService accountService;


    @Transactional
    public SlotListPageDTO getQuestsAndSlots(LocalDate date, Principal principal) {
        Account account = accountService.getAccountByLogin(principal.getName());
        Set<Quest> quests = new TreeSet<>(questService.findAllByAccountId(account.getId()));

        Map<String, List<Slot>> questsAndSlots = new LinkedHashMap<>();
        Set<StatusType> useStatuses = new HashSet<>();

        for (Quest quest : quests) {
            Map<LocalTime, Reservation> reservations = reservationService.findActiveByQuestIdAndDate(quest.getId(), date);
            Set<StatusType> statusTypes = getUniqueStatusTypes(reservations.values());
            useStatuses.addAll(statusTypes);
            SlotList slotList = SlotListMapper.createObject(quest.getSlotList());
            SlotFactory slotFactory = new SlotFactory(quest, date, slotList, reservations);
            List<Slot> slots = slotFactory.getActualSlots();
            questsAndSlots.put(quest.getQuestName(), slots);
        }

        return new SlotListPageDTO(questsAndSlots, useStatuses);
    }

    // TODO: оптимизировать поиск уникальных статусов
    private Set<StatusType> getUniqueStatusTypes(Collection<Reservation> reservations) {
        return reservations.stream()
                .map(Reservation::getStatusType)
                .collect(Collectors.toSet());
    }
}
