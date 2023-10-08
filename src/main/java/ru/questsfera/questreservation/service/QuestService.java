package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Status;
import ru.questsfera.questreservation.entity.User;
import ru.questsfera.questreservation.repository.QuestRepository;
import ru.questsfera.questreservation.repository.ReservationRepository;

import java.util.List;
import java.util.Set;

@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public QuestService(QuestRepository questRepository, ReservationRepository reservationRepository) {
        this.questRepository = questRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public List<Quest> getQuestsByAdmin(Admin admin) {
        return questRepository.findAllByAdminOrderByQuestName(admin);
    }

    @Transactional
    public boolean existQuestNameByAdmin(String questName, Admin admin) {
        return questRepository.existsQuestByQuestNameAndAdmin(questName, admin);
    }

    @Transactional
    public boolean existQuestByAdmin(Quest quest, Admin admin) {
        return questRepository.existsQuestByIdAndAdmin(quest.getId(), admin);
    }

    @Transactional
    public boolean hasReservations(Quest quest) {
        return reservationRepository.existsByQuest(quest);
    }

    @Transactional
    public void saveQuest(Quest quest) {
        quest.saveUsers();
        questRepository.save(quest);
    }

    @Transactional
    public void deleteQuest(Quest quest, Admin admin) {

        checkSecurityForQuest(quest, admin);

        reservationRepository.deleteByQuest(quest);

        for (User user : quest.getUsers()) {
            user.deleteQuestForUser(quest);
        }

        for (Status status : quest.getStatuses()) {
            status.deleteQuestForStatus(quest);
        }

        if (!quest.getSynchronizedQuests().isEmpty()) {
            dontSynchronizeQuests(quest);
            System.out.println("Синхронизация по квесту id:" + quest.getId()
                    + " и всем связаным квестам отменена"); // вывести сообщение на страницу
        }
        admin.deleteQuestForAdmin(quest);
        questRepository.delete(quest);
    }

    @Transactional
    public void checkSecurityForQuest(Quest quest, Admin admin) {
        boolean existQuestByAdmin = existQuestByAdmin(quest, admin);
        if (!existQuestByAdmin) {
            throw new SecurityException("Нет доступа для изменения данного квеста");
        }
    }

    //***SynchronizeQuests
    @Transactional
    public void synchronizeQuests(Quest... quests) {
        Quest.synchronizeQuests(quests);
    }

    @Transactional
    public Set<Quest> getSynchronizedQuests(Quest quest) {
        return quest.getSynchronizedQuests();
    }

    @Transactional
    public void dontSynchronizeQuests(Quest quest) {
        Quest.dontSynchronizeQuests(quest);
    }
}
