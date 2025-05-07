package ru.questsfera.questreservation.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.model.dto.QuestFormDTO;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quests")
public class Quest implements Comparable<Quest> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quests_seq")
    @SequenceGenerator(name = "quests_seq", sequenceName = "quests_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "quest_name")
    private String questName;

    @Column(name = "min_persons")
    private Integer minPersons;

    @Column(name = "max_persons")
    private Integer maxPersons;

    @Column(name = "auto_block")
    private LocalTime autoBlock;

    @Column(name = "sms")
    private String sms;

    @Column(name = "slot_list")
    private String slotList;

    @Column(name = "company_id")
    private Integer companyId;

    @ManyToMany(mappedBy = "quests")
    private List<Account> accounts = new ArrayList<>();

    @Column(name = "statuses")
    private String statuses;

    @ManyToMany
    @JoinTable(name = "synchronized_quests",
            joinColumns = @JoinColumn(name = "id_first_quest"),
            inverseJoinColumns = @JoinColumn(name = "id_second_quest"))
    private Set<Quest> synchronizedQuests = new HashSet<>();

    public static Quest fromQuestFormAndCompanyId(QuestFormDTO questFormDTO, Integer companyId) {
        Quest quest = new Quest();
        quest.questName = questFormDTO.getQuestName();
        quest.minPersons = questFormDTO.getMinPersons();
        quest.maxPersons = questFormDTO.getMaxPersons();
        quest.autoBlock = questFormDTO.getAutoBlock();
        quest.slotList = SlotListMapper.createJSON(questFormDTO.getSlotList());
        quest.accounts = questFormDTO.getAccounts();
        quest.statuses = questFormDTO.getStatuses().stream() // TODO через MapStruct ?
                .map(s -> s.getType().name())
                .collect(Collectors.joining(","));
        quest.companyId = companyId;
        return quest;
    }

    public static void synchronizeQuests(Quest... quests) {
        Set<Quest> questSet = new HashSet<>(List.of(quests));

        if (!validatorToSynchronize(questSet)) return;

        //TODO: написать валидатор по структуре слотов

        for (Quest quest : questSet) {
            quest.getSynchronizedQuests().addAll(questSet);
        }
    }

    private static boolean validatorToSynchronize(Set<Quest> questSet) {
        boolean check = true;
        Integer validCompanyId = questSet.iterator().next().getCompanyId();

        if (questSet.size() < 2) {
            throw new RuntimeException("Попытка синхронизировать меньше двух квестов");
        }

        for (Quest quest : questSet) {
            if (!quest.getCompanyId().equals(validCompanyId)) {
                throw new RuntimeException("Попытка синхронизировать " +
                        "квесты у разных компаний");
            }
            if (!quest.getSynchronizedQuests().isEmpty() || !check) {
                check = false;
                if (!quest.getSynchronizedQuests().equals(questSet)) {
                    throw new RuntimeException("Попытка синхронизировать " +
                            "ранее синхронизированные квесты");
                }
            }
        }
        /* check == false, если был дублированный запрос */
        return check;
    }

    public static void dontSynchronizeQuests(Quest quest) {
        for (Quest syncQuest : quest.getSynchronizedQuests()) {
            if (syncQuest.equals(quest)) continue;
            syncQuest.getSynchronizedQuests().clear();
        }
        quest.getSynchronizedQuests().clear();
    }

    @Override
    public int compareTo(Quest o) {
        return o.getQuestName().compareTo(this.questName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quest quest)) return false;
        return id != null&& Objects.equals(getId(), quest.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
