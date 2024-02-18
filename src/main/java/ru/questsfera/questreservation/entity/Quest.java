package ru.questsfera.questreservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.converter.SlotListMapper;
import ru.questsfera.questreservation.dto.QuestForm;

import java.time.LocalTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "quests", schema = "quest_reservations_db")
@JsonIgnoreProperties({"autoBlock", "sms", "users", "slotList", "admin", "synchronizedQuests"})
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
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToMany(mappedBy = "quests")
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "status_quest",
            joinColumns = @JoinColumn(name = "quest_id"),
            inverseJoinColumns = @JoinColumn(name = "status_id"))
    private Set<Status> statuses = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "synchronized_quests",
            joinColumns = @JoinColumn(name = "id_first_quest"),
            inverseJoinColumns = @JoinColumn(name = "id_second_quest"))
    private Set<Quest> synchronizedQuests = new HashSet<>();

    public Quest(QuestForm questForm, Admin admin) {
        this.questName = questForm.getQuestName();
        this.minPersons = questForm.getMinPersons();
        this.maxPersons = questForm.getMaxPersons();
        this.autoBlock = questForm.getAutoBlock();
        this.slotList = SlotListMapper.createJSON(questForm.getSlotList());
        this.users = questForm.getUsers();
        this.statuses = questForm.getStatuses();
        this.admin = admin;
    }

    public void addStatusForQuest(Status status) {
        status.getQuests().add(this);
        this.statuses.add(status);
    }

    public void saveUsers() {
        users.forEach(user -> user.getQuests().add(this));
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
        Admin validAdmin = questSet.iterator().next().getAdmin();

        if (questSet.size() < 2) {
            throw new RuntimeException("Попытка синхронизировать меньше двух квестов");
        }

        for (Quest quest : questSet) {
            if (!quest.getAdmin().equals(validAdmin)) {
                throw new RuntimeException("Попытка синхронизировать " +
                        "квесты у разных админов");
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
