package ru.questsfera.questreservation.cache.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private Integer id;
    private String questName;
    private Integer minPersons;
    private Integer maxPersons;
    private LocalTime autoBlock;
    private String sms;
    private SlotList slotList;
    private Integer companyId;
    private Set<Status> statuses;
    private Set<Integer> synchronizedQuestIds;

    public QuestCache(Quest quest) {
        this.id = quest.getId();
        this.questName = quest.getQuestName();
        this.minPersons = quest.getMinPersons();
        this.maxPersons = quest.getMaxPersons();
        this.autoBlock = quest.getAutoBlock();
        this.sms = quest.getSms();
        this.slotList = SlotListMapper.createObject(quest.getSlotList());
        this.companyId = quest.getCompany().getId();
        this.statuses = quest.getStatuses();
        this.synchronizedQuestIds = quest.getSynchronizedQuests().stream().map(Quest::getId).collect(Collectors.toSet());
    }

    @Override
    @JsonIgnore
    public String getCacheId() {
        return "quest:%d".formatted(this.getId());
    }
}
