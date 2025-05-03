package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.converter.QuestMapper;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.dto.*;
import ru.questsfera.questreservation.entity.Quest;
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
    private final QuestMapper questMapper;

    @Transactional(readOnly = true)
    public SlotListPageDTO getQuestsAndSlotsByDate(LocalDate date, Principal principal) {
        Set<Quest> quests = questService.findAllByAccount_login(principal.getName());

        List<ReservationDTO> reservationDTOs = reservationService.findActiveByQuestIdsAndDate(
                quests.stream().map(Quest::getId).toList(), date);

        Map<Integer, Map<LocalTime, ReservationDTO>> questIdAndTimeAndReserve =
                splitQuestIdAndTimeAndReserve(reservationDTOs);

        Map<String, List<Slot>> questNamesAndSlots = getQuestNamesAndSlots(quests, questIdAndTimeAndReserve, date);

        Set<StatusType> useStatuses = reservationDTOs.stream()
                .map(ReservationDTO::getStatusType)
                .collect(Collectors.toSet());

        return new SlotListPageDTO(questNamesAndSlots, useStatuses);
    }

    private Map<Integer, Map<LocalTime, ReservationDTO>> splitQuestIdAndTimeAndReserve(List<ReservationDTO> reservations) {
        Map<Integer, Map<LocalTime, ReservationDTO>> questIdAndTimeAndReserve = new HashMap<>();

        for (ReservationDTO resDTO : reservations) {
            Map<LocalTime, ReservationDTO> timeAndReserve = questIdAndTimeAndReserve.get(resDTO.getQuestId());
            if (timeAndReserve != null) {
                // TODO create notification mechanism for double blocking
                if (timeAndReserve.containsKey(resDTO.getTimeReserve())) {
                    throw new RuntimeException(String.format("Double reservation: reservation_id=%s and reservation_id=%s",
                            resDTO.getId(), timeAndReserve.get(resDTO.getTimeReserve())));
                }
                timeAndReserve.put(resDTO.getTimeReserve(), resDTO);
            } else {
                questIdAndTimeAndReserve.put(resDTO.getQuestId(), new HashMap<>(Map.of(resDTO.getTimeReserve(), resDTO)));
            }
        }

        return questIdAndTimeAndReserve;
    }

    private Map<String, List<Slot>> getQuestNamesAndSlots(
            Set<Quest> quests,
            Map<Integer, Map<LocalTime, ReservationDTO>> questIdAndTimeAndReserve,
            LocalDate date
    ) {
        Map<String, List<Slot>> questNameAndSlots = new LinkedHashMap<>();

        for (Quest quest : quests) {
            Map<LocalTime, ReservationDTO> reservations = questIdAndTimeAndReserve.getOrDefault(quest.getId(), Collections.emptyMap());
            SlotList slotList = SlotListMapper.createObject(quest.getSlotList());
            QuestDTO questDTO = questMapper.toDto(quest);
            SlotFactory slotFactory = new SlotFactory(questDTO, date, slotList, reservations);
            List<Slot> slots = slotFactory.getActualSlots();
            questNameAndSlots.put(quest.getQuestName(), slots);
        }

        return questNameAndSlots;
    }
}
