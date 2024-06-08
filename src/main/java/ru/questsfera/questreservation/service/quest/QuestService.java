package ru.questsfera.questreservation.service.quest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Status;
import ru.questsfera.questreservation.repository.AccountRepository;
import ru.questsfera.questreservation.repository.QuestRepository;
import ru.questsfera.questreservation.repository.ReservationRepository;
import ru.questsfera.questreservation.repository.StatusRepository;

import java.util.List;

@Service
public class QuestService {

    @Autowired
    private QuestRepository questRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StatusRepository statusRepository;

    @Transactional
    public List<Quest> getQuestsByCompanyId(Integer companyId) {
        return questRepository.findAllByCompanyIdOrderByQuestName(companyId);
    }

    @Transactional
    public List<Quest> findAllByAccountId(Integer accountId) {
        return questRepository.findAllByAccountId(accountId);
    }

    @Transactional
    public List<Quest> findAll() {
        return questRepository.findAll();
    }

    @Transactional
    public boolean existQuestNameByCompany(String questName, Integer companyId) {
        return questRepository.existsQuestByQuestNameAndCompanyId(questName, companyId);
    }

    @Transactional
    public boolean existQuestByAccountId(Quest quest, Integer accountId) {
        return questRepository.existsQuestByIdAndAccountId(quest.getId(), accountId);
    }

    //TODO: migration in reservationService
    @Transactional
    public boolean hasReservationsByQuest(Quest quest) {
        return reservationRepository.existsByQuest(quest);
    }

    @Transactional
    public void saveQuest(Quest quest) {
        for (Account account : accountRepository.findAllByQuestId(quest.getId())) {
            account.getQuests().add(quest);
            accountRepository.save(account);
        }
        questRepository.save(quest);
    }

    @Transactional
    public void deleteQuest(Quest quest, Integer accountId) {

        checkSecurityForQuest(quest, accountId);

        reservationRepository.deleteByQuest(quest);

        for (Account account : accountRepository.findAllByQuestId(quest.getId())) {
            account.getQuests().remove(quest);
            accountRepository.save(account);
        }

        for (Status status : statusRepository.findAllByQuestId(quest.getId())) {
            status.getQuests().remove(quest);
            statusRepository.save(status);
        }

//        if (!quest.getSynchronizedQuests().isEmpty()) {
//            dontSynchronizeQuests(quest);
//            System.out.println("Синхронизация по квесту id:" + quest.getId()
//                    + " и всем связаным квестам отменена"); //TODO: вывести сообщение на страницу
//        }
        questRepository.delete(quest);
    }

    @Transactional
    public void checkSecurityForQuest(Quest quest, Integer accountId) {
        boolean existQuestByCompany = existQuestByAccountId(quest, accountId);
        if (!existQuestByCompany) {
            throw new SecurityException("Нет доступа для изменения данного квеста");
        }
    }


    //TODO: SynchronizeQuests
//    @Transactional
//    public void synchronizeQuests(Quest... quests) {
//        Quest.synchronizeQuests(quests);
//    }
//
//    @Transactional
//    public Set<Quest> getSynchronizedQuests(Quest quest) {
//        return quest.getSynchronizedQuests();
//    }
//
//    @Transactional
//    public void dontSynchronizeQuests(Quest quest) {
//        Quest.dontSynchronizeQuests(quest);
//    }
}
