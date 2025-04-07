package ru.questsfera.questreservation.service.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    public SlotListPageDTO getQuestsAndSlotsByDate(LocalDate date, Principal principal) {
        List<Quest> quests = questService.findAllByAccount_login(principal.getName());

        List<ReservationDTO> reservationDTOs = reservationService.findActiveByQuestIdsAndDate(
                quests.stream().map(Quest::getId).toList(), date);

        Map<Integer, Map<LocalTime, ReservationDTO>> questIdsAndReservations = new HashMap<>();

        for (ReservationDTO resDTO : reservationDTOs) {
            Map<LocalTime, ReservationDTO> timeAndReserve = questIdsAndReservations.get(resDTO.getQuestId());
            if (timeAndReserve != null) {
                if (timeAndReserve.containsKey(resDTO.getTimeReserve())) {
                    throw new RuntimeException(String.format("Double reservation: reservation_id=%s and reservation_id=%s",
                            resDTO.getId(), timeAndReserve.get(resDTO.getTimeReserve())));
                }
                timeAndReserve.put(resDTO.getTimeReserve(), resDTO);
            } else {
                questIdsAndReservations.put(resDTO.getQuestId(), new HashMap<>(Map.of(resDTO.getTimeReserve(), resDTO)));
            }
        }

        Map<String, List<Slot>> questsAndSlots = new LinkedHashMap<>();

        for (Quest quest : quests) {
            Map<LocalTime, ReservationDTO> reservations = questIdsAndReservations.getOrDefault(quest.getId(), Collections.emptyMap());
            SlotList slotList = SlotListMapper.createObject(quest.getSlotList());
            SlotFactory slotFactory = new SlotFactory(new QuestDTO(quest), date, slotList, reservations);
            List<Slot> slots = slotFactory.getActualSlots();
            questsAndSlots.put(quest.getQuestName(), slots);
        }

        Set<StatusType> useStatuses = reservationDTOs.stream()
                .map(ReservationDTO::getStatusType)
                .collect(Collectors.toSet());

        return new SlotListPageDTO(questsAndSlots, useStatuses);
    }
}
