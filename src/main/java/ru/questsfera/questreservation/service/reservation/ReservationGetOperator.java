package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.mapper.SlotListJsonMapper;
import ru.questsfera.questreservation.model.dto.*;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.processor.SlotFactory;
import ru.questsfera.questreservation.security.AccountUserDetails;
import ru.questsfera.questreservation.service.quest.QuestService;

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
    private final SlotListJsonMapper slotListJsonMapper;
    private final SlotFactory slotFactory;

    @Transactional(readOnly = true)
    public SlotListPage getQuestsAndSlotsByDate(LocalDate date, AccountUserDetails principal) {
        Set<Quest> quests = questService.findAllByAccount_id(principal.getId());

        List<ReservationWithClient> activeReservationsWithClient = reservationService.findActiveByQuestIdsAndDate(
                quests.stream().map(Quest::getId).toList(), date);

        Map<Integer, Map<LocalTime, ReservationWithClient>> questIdAndTimeAndReserve =
                splitQuestIdAndTimeAndReserve(activeReservationsWithClient);

        Map<String, List<Slot>> questNamesAndSlots = getQuestNamesAndSlots(quests, questIdAndTimeAndReserve, date);

        Set<Status> useStatuses = activeReservationsWithClient.stream()
                .map(ReservationWithClient::getStatus)
                .collect(Collectors.toSet());

        return new SlotListPage(questNamesAndSlots, useStatuses);
    }

    private Map<Integer, Map<LocalTime, ReservationWithClient>> splitQuestIdAndTimeAndReserve(List<ReservationWithClient> reservations) {
        Map<Integer, Map<LocalTime, ReservationWithClient>> questIdAndTimeAndReserve = new HashMap<>();

        for (ReservationWithClient resWithClient : reservations) {
            Map<LocalTime, ReservationWithClient> timeAndReserve = questIdAndTimeAndReserve.get(resWithClient.getQuestId());
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
            Map<Integer, Map<LocalTime, ReservationWithClient>> questIdAndTimeAndReserve,
            LocalDate date
    ) {
        Map<String, List<Slot>> questNameAndSlots = new LinkedHashMap<>();

        for (Quest quest : quests) {
            Map<LocalTime, ReservationWithClient> reservations = questIdAndTimeAndReserve.getOrDefault(quest.getId(), Collections.emptyMap());
            SlotList slotList = slotListJsonMapper.toObject(quest.getSlotList());
            List<Slot> slots = slotFactory.getSlots(quest, date, slotList, reservations);
            questNameAndSlots.put(quest.getQuestName(), slots);
        }

        return questNameAndSlots;
    }
}
