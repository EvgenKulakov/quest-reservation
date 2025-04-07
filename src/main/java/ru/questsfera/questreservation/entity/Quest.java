package ru.questsfera.questreservation.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.dto.QuestForm;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
@Entity
@Table(name = "quests", schema = "quest_reservations_db")
public class Quest implements Comparable<Quest> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(mappedBy = "quests")
    private List<Account> accounts = new ArrayList<>();

//    @ManyToMany
//    @JoinTable(name = "status_quest",
//            joinColumns = @JoinColumn(name = "quest_id"),
//            inverseJoinColumns = @JoinColumn(name = "status_id"))
    @Column(name = "statuses")
    private String statuses;

    @ManyToMany
    @JoinTable(name = "synchronized_quests",
            joinColumns = @JoinColumn(name = "id_first_quest"),
            inverseJoinColumns = @JoinColumn(name = "id_second_quest"))
    private Set<Quest> synchronizedQuests = new HashSet<>();

    public Quest(QuestForm questForm, Company company) {
        this.questName = questForm.getQuestName();
        this.minPersons = questForm.getMinPersons();
        this.maxPersons = questForm.getMaxPersons();
        this.autoBlock = questForm.getAutoBlock();
        this.slotList = SlotListMapper.createJSON(questForm.getSlotList());
        this.accounts = questForm.getAccounts();
        this.statuses = questForm.getStatuses().stream()
                .map(s -> s.getType().name()).collect(Collectors.joining(","));
        this.company = company;
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
        Company validCompany = questSet.iterator().next().getCompany();

        if (questSet.size() < 2) {
            throw new RuntimeException("Попытка синхронизировать меньше двух квестов");
        }

        for (Quest quest : questSet) {
            if (!quest.getCompany().equals(validCompany)) {
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
