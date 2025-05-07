package ru.questsfera.questreservation.model.dto;

import lombok.*;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.model.entity.Status;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestDTO implements Comparable<QuestDTO> {

    private Integer id;
    private String questName;
    private Integer minPersons;
    private Integer maxPersons;
    private LocalTime autoBlock;
    private String sms;
    private String slotList;
    private Integer companyId;
    private List<Account> accounts;
    private List<Status> statuses;
    private Set<Quest> synchronizedQuests;

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
