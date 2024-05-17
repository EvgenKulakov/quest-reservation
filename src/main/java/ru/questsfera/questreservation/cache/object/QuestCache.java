package ru.questsfera.questreservation.cache.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.dto.SlotList;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Status;

import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class QuestCache implements Cache {

    private String cacheId;
    private String questName;
    private Integer minPersons;
    private Integer maxPersons;
    private LocalTime autoBlock;
    private String sms;
    private SlotList slotList;
    private Integer companyId;
    private Set<Status> statuses;
    private Set<String> synchronizedQuestIds;

    public QuestCache(Quest quest) {
        this.cacheId = "quest:%d".formatted(quest.getId());
        this.questName = quest.getQuestName();
        this.minPersons = quest.getMinPersons();
        this.maxPersons = quest.getMaxPersons();
        this.autoBlock = quest.getAutoBlock();
        this.sms = quest.getSms();
        this.slotList = SlotListMapper.createObject(quest.getSlotList());
        this.companyId = quest.getCompany().getId();
        this.statuses = quest.getStatuses();
        this.synchronizedQuestIds = quest.getSynchronizedQuests()
                .stream()
                .map(q -> "quest:%d".formatted(q.getId()))
                .collect(Collectors.toSet());
    }
}
