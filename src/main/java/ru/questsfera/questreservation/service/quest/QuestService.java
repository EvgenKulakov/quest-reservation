package ru.questsfera.questreservation.service.quest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.repository.jpa.AccountRepository;
import ru.questsfera.questreservation.repository.jpa.QuestRepository;
import ru.questsfera.questreservation.repository.jpa.ReservationRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final ReservationRepository reservationRepository;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Optional<Quest> findById(Integer id) {
        return questRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Quest> getQuestsByCompany(Integer companyId) {
        return questRepository.findAllByCompanyIdOrderByQuestName(companyId);
    }

    @Transactional
    public List<Quest> findAllByAccountId(Integer accountId) {
        return questRepository.findAllByAccountId(accountId);
    }

    @Transactional(readOnly = true)
    public Set<Quest> findAllByAccount_login(String login) {
        return questRepository.findAllByAccount_login(login);
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
    public boolean existQuestByCompany(Quest quest, Integer companyId) {
        return questRepository.existsQuestByIdAndCompanyId(quest.getId(), companyId);
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
    public void deleteQuest(Quest quest, Integer companyId) {

        checkSecurityForQuest(quest, companyId);

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
    public void checkSecurityForQuest(Quest quest, Integer companyId) {
        boolean existQuestByCompany = existQuestByCompany(quest, companyId);
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
