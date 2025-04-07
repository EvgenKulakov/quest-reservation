package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.dto.*;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.SlotFactory;
import ru.questsfera.questreservation.service.quest.QuestService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationGetOperator {

    private final ReservationService reservationService;
    private final QuestService questService;

    @Transactional(readOnly = true)
    public SlotListPageDTO getQuestsAndSlotsByDate(LocalDate date, Principal principal) {
        Set<Quest> quests = new TreeSet<>(questService.findAllByAccount_login(principal.getName()));

//        List<ReservationDTO> reservationDTOS = reservationService
//                .findActiveByQuestIdsAndDateOrderByQuestAndTimeReserve(
//                        quests.stream().map(Quest::getId).toList(), date);

        Map<String, List<Slot>> questsAndSlots = new LinkedHashMap<>();
        Set<StatusType> useStatuses = new HashSet<>();

        for (Quest quest : quests) {
            Map<LocalTime, Reservation> reservations = reservationService.findActiveByQuestIdAndDate(quest.getId(), date);
            Set<StatusType> statusTypes = getUniqueStatusTypes(reservations.values());
            useStatuses.addAll(statusTypes);
            SlotList slotList = SlotListMapper.createObject(quest.getSlotList());
            SlotFactory slotFactory = new SlotFactory(new QuestDTO(quest), date, slotList, reservations);
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
