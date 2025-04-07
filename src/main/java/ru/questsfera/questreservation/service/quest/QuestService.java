package ru.questsfera.questreservation.service.quest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.repository.AccountRepository;
import ru.questsfera.questreservation.repository.QuestRepository;
import ru.questsfera.questreservation.repository.ReservationRepository;

import java.util.List;

@Service
public class QuestService {

    @Autowired
    private QuestRepository questRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public List<Quest> getQuestsByCompany(Company company) {
        return questRepository.findAllByCompanyOrderByQuestName(company);
    }

    @Transactional
    public List<Quest> findAllByAccountId(Integer accountId) {
        return questRepository.findAllByAccountId(accountId);
    }

    @Transactional(readOnly = true)
    public List<Quest> findAllByAccount_login(String login) {
        return questRepository.findAllByAccount_login(login);
    }

    @Transactional
    public List<Quest> findAll() {
        return questRepository.findAll();
    }

    @Transactional
    public boolean existQuestNameByCompany(String questName, Company company) {
        return questRepository.existsQuestByQuestNameAndCompany(questName, company);
    }

    @Transactional
    public boolean existQuestByCompany(Quest quest, Company company) {
        return questRepository.existsQuestByIdAndCompany(quest.getId(), company);
    }

    //TODO: migration in reservationService
    @Transactional
    public boolean hasReservationsByQuest(Quest quest) {
        return reservationRepository.existsByQuestId(quest.getId());
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
    public void deleteQuest(Quest quest, Company company) {

        checkSecurityForQuest(quest, company);

        reservationRepository.deleteByQuestId(quest.getId());

        for (Account account : accountRepository.findAllByQuestId(quest.getId())) {
            account.getQuests().remove(quest);
            accountRepository.save(account);
        }

//        for (Status status : statusRepository.findAllByQuestId(quest.getId())) {
////            status.getQuests().remove(quest);
//            statusRepository.save(status);
//        }

//        if (!quest.getSynchronizedQuests().isEmpty()) {
//            dontSynchronizeQuests(quest);
//            System.out.println("Синхронизация по квесту id:" + quest.getId()
//                    + " и всем связаным квестам отменена"); //TODO: вывести сообщение на страницу
//        }
        questRepository.delete(quest);
    }

    @Transactional
    public void checkSecurityForQuest(Quest quest, Company company) {
        boolean existQuestByCompany = existQuestByCompany(quest, company);
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
