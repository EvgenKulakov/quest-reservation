package ru.questsfera.questreservation.service.quest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.repository.jpa.QuestRepository;
import ru.questsfera.questreservation.repository.jpa.ReservationRepository;
import ru.questsfera.questreservation.service.account.AccountService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final ReservationRepository reservationRepository;
    private final AccountService accountService;

    @Transactional(readOnly = true)
    public List<Quest> getQuestsByCompany(Integer companyId) {
        return questRepository.findAllByCompanyIdOrderByQuestName(companyId);
    }

    @Transactional(readOnly = true)
    public Set<Quest> findAllByAccount_id(Integer accountId) {
        return questRepository.findAllByAccount_id(accountId);
    }

    @Transactional
    public boolean existQuestNameByCompany(String questName, Integer companyId) {
        return questRepository.existsQuestByQuestNameAndCompanyId(questName, companyId);
    }

    @Transactional
    public boolean existQuestByCompany(Quest quest, Integer companyId) {
        return questRepository.existsQuestByIdAndCompanyId(quest.getId(), companyId);
    }

    @Transactional
    public void saveNewQuest(Quest quest, Account myAccount) {
        questRepository.save(quest);
        for (Account account : quest.getAccounts()) {
            if (!account.getQuests().contains(quest)) {
                account.getQuests().add(quest);
                accountService.saveAccount(account);
                if (account.equals(myAccount)) accountService.updateCurrentAccount(myAccount);
            }
        }
    }

    @Transactional
    public void deleteQuest(Quest quest) {
        reservationRepository.deleteByQuestId(quest.getId());

        for (Account account : accountService.getAccountsByQuestId(quest.getId())) {
            account.getQuests().remove(quest);
            accountService.saveAccount(account);
        }

//        if (!quest.getSynchronizedQuests().isEmpty()) { // TODO SynchronizeQuests
//            dontSynchronizeQuests(quest);
//            System.out.println("Синхронизация по квесту id:" + quest.getId()
//                    + " и всем связаным квестам отменена");
//        }
        questRepository.deleteById(quest.getId());
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
