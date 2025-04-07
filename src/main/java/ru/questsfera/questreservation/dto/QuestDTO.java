package ru.questsfera.questreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Status;

import java.time.LocalTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
public class QuestDTO implements Comparable<QuestDTO> {

    private Integer id;
    private String questName;
    private Integer minPersons;
    private Integer maxPersons;
    private LocalTime autoBlock;
    private String sms;
    private String slotList;
    private Company company;
    private List<Account> accounts;
    private List<Status> statuses;
    private Set<Quest> synchronizedQuests;

    public QuestDTO(QuestForm questForm, Company company) {
        this.questName = questForm.getQuestName();
        this.minPersons = questForm.getMinPersons();
        this.maxPersons = questForm.getMaxPersons();
        this.autoBlock = questForm.getAutoBlock();
        this.slotList = SlotListMapper.createJSON(questForm.getSlotList());
        this.accounts = questForm.getAccounts();
        this.statuses = questForm.getStatuses();
        this.company = company;
    }

    public QuestDTO(Quest quest) { // TODO слой парсинга ДТО
        this.id = quest.getId();
        this.questName = quest.getQuestName();
        this.minPersons = quest.getMinPersons();
        this.maxPersons = quest.getMaxPersons();
        this.autoBlock = quest.getAutoBlock();
        this.sms = quest.getSms();
        this.slotList = quest.getSlotList();
        this.company = quest.getCompany();
        this.accounts = quest.getAccounts();
        this.statuses = Arrays.stream(quest.getStatuses().split(",")).map(Status::createStatusFromStatusTypeName).toList();
        this.synchronizedQuests = quest.getSynchronizedQuests();
    }

    @Override
    public int compareTo(QuestDTO o) {
        return o.getQuestName().compareTo(this.questName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestDTO quest)) return false;
        return id != null&& Objects.equals(getId(), quest.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
