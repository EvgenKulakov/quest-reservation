package ru.questsfera.questreservation.entity;

import jakarta.persistence.*;

import java.sql.Time;
import java.util.*;

@Entity
@Table(name = "quests", schema = "quest_reservations")
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quest_name")
    private String questName;

    @Column(name = "slot_list")
    private String slotList;

    @Column(name = "min_persons")
    private int minPersons;

    @Column(name = "max_persons")
    private int maxPersons;

    @Column(name = "auto_block")
    private Time autoBlock;

    @Column(name = "sms")
    private String sms;

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

    @ManyToMany
    @JoinTable(name = "synchronized_quests",
            joinColumns = @JoinColumn(name = "id_first_quest"),
            inverseJoinColumns = @JoinColumn(name = "id_second_quest"))
    private Set<Quest> synchronizedQuests = new HashSet<>();

    public Quest() {}

    public Quest(String questName, String slotList, int minPersons, int maxPersons) {
        this.questName = questName;
        this.slotList = slotList;
        this.minPersons = minPersons;
        this.maxPersons = maxPersons;
    }

    public void addStatusForQuest(Status status) {
        status.getQuests().add(this);
        statuses.add(status);
    }

    public void deleteStatusForQuest(Status status) {
        status.getQuests().remove(this);
        statuses.remove(status);
    }

    public static void synchronizeQuests(Quest... quests) {
        Set<Quest> questSet = new HashSet<>(List.of(quests));

        if (!validatorToSynchronize(questSet)) return;

        // написать валидатор по структуре слотов

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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Quest> getSynchronizedQuests() {
        return synchronizedQuests;
    }

    public void setSynchronizedQuests(Set<Quest> synchronizedQuests) {
        this.synchronizedQuests = synchronizedQuests;
    }

    public Set<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<Status> statuses) {
        this.statuses = statuses;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String getSlotList() {
        return slotList;
    }

    public void setSlotList(String slotList) {
        this.slotList = slotList;
    }

    public int getMinPersons() {
        return minPersons;
    }

    public void setMinPersons(int minPersons) {
        this.minPersons = minPersons;
    }

    public int getMaxPersons() {
        return maxPersons;
    }

    public void setMaxPersons(int maxPersons) {
        this.maxPersons = maxPersons;
    }

    public Time getAutoBlock() {
        return autoBlock;
    }

    public void setAutoBlock(Time autoBlock) {
        this.autoBlock = autoBlock;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
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

    @Override
    public String toString() {
        return "Quest{" +
                "id=" + id +
                ", questName='" + questName + '\'' +
                ", slotList length='" + (slotList != null ? slotList.length() : null) + '\'' +
                ", minPersons=" + minPersons +
                ", maxPersons=" + maxPersons +
                ", autoBlock=" + autoBlock +
                ", sms='" + sms + '\'' +
                ", admin=" + (admin != null ? admin.getUsername() : null);
    }
}
