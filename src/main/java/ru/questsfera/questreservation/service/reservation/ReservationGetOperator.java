package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.mapper.SlotListJsonMapper;
import ru.questsfera.questreservation.model.dto.*;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.processor.SlotFactory;
import ru.questsfera.questreservation.service.quest.QuestService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationGetOperator {

    private final ReservationService reservationService;
    private final QuestService questService;

    @Transactional(readOnly = true)
    public SlotListPage getQuestsAndSlotsByDate(LocalDate date, Principal principal) {
        Set<Quest> quests = questService.findAllByAccount_login(principal.getName());

        List<ReservationWIthClient> reservationsWithClient = reservationService.findActiveByQuestIdsAndDate(
                quests.stream().map(Quest::getId).toList(), date);

        Map<Integer, Map<LocalTime, ReservationWIthClient>> questIdAndTimeAndReserve =
                splitQuestIdAndTimeAndReserve(reservationsWithClient);

        Map<String, List<Slot>> questNamesAndSlots = getQuestNamesAndSlots(quests, questIdAndTimeAndReserve, date);

        Set<Status> useStatuses = reservationsWithClient.stream()
                .map(ReservationWIthClient::getStatus)
                .collect(Collectors.toSet());

        return new SlotListPage(questNamesAndSlots, useStatuses);
    }

    private Map<Integer, Map<LocalTime, ReservationWIthClient>> splitQuestIdAndTimeAndReserve(List<ReservationWIthClient> reservations) {
        Map<Integer, Map<LocalTime, ReservationWIthClient>> questIdAndTimeAndReserve = new HashMap<>();

        for (ReservationWIthClient resWithClient : reservations) {
            Map<LocalTime, ReservationWIthClient> timeAndReserve = questIdAndTimeAndReserve.get(resWithClient.getQuestId());
            if (timeAndReserve != null) {
                // TODO create notification mechanism for double blocking
                if (timeAndReserve.containsKey(resWithClient.getTimeReserve())) {
                    throw new RuntimeException(String.format("Double reservation: reservation_id=%s and reservation_id=%s",
                            resWithClient.getId(), timeAndReserve.get(resWithClient.getTimeReserve())));
                }
                timeAndReserve.put(resWithClient.getTimeReserve(), resWithClient);
            } else {
                questIdAndTimeAndReserve.put(resWithClient.getQuestId(), new HashMap<>(Map.of(resWithClient.getTimeReserve(), resWithClient)));
            }
        }

        return questIdAndTimeAndReserve;
    }

    private Map<String, List<Slot>> getQuestNamesAndSlots(
            Set<Quest> quests,
            Map<Integer, Map<LocalTime, ReservationWIthClient>> questIdAndTimeAndReserve,
            LocalDate date
    ) {
        Map<String, List<Slot>> questNameAndSlots = new LinkedHashMap<>();

        for (Quest quest : quests) {
            Map<LocalTime, ReservationWIthClient> reservations = questIdAndTimeAndReserve.getOrDefault(quest.getId(), Collections.emptyMap());
            SlotList slotList = SlotListJsonMapper.toObject(quest.getSlotList());
            SlotFactory slotFactory = new SlotFactory(quest, date, slotList, reservations);
            List<Slot> slots = slotFactory.getActualSlots();
            questNameAndSlots.put(quest.getQuestName(), slots);
        }

        return questNameAndSlots;
    }
}
