package ru.questsfera.questreservation.service.quest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.repository.jpa.AccountRepository;
import ru.questsfera.questreservation.repository.jpa.QuestRepository;
import ru.questsfera.questreservation.repository.jpa.ReservationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestServiceTest {

    @Mock QuestRepository questRepository;
    @Mock ReservationRepository reservationRepository;
    @Mock AccountRepository accountRepository;
    @InjectMocks QuestService questService;

    @Test
    void getQuestsByCompany() {
        List<Quest> exceptedQuests = List.of(getQuest());
        when(questService.getQuestsByCompany(1)).thenReturn(exceptedQuests);
        List<Quest> actualQuests = questService.getQuestsByCompany(1);

        assertThat(actualQuests).isSameAs(exceptedQuests);

        verify(questRepository).findAllByCompanyIdOrderByQuestName(1);
    }

    @Test
    void findAllByAccount_login() {
        String accountLogin = "login";
        Set<Quest> exceptedQuests = Set.of(getQuest());
        when(questRepository.findAllByAccount_login(accountLogin)).thenReturn(exceptedQuests);
        Set<Quest> actualQuests = questService.findAllByAccount_login(accountLogin);

        assertThat(actualQuests).isSameAs(exceptedQuests);

        verify(questRepository).findAllByAccount_login(accountLogin);
    }

    @Test
    void existQuestNameByCompany() {
        Quest quest = getQuest();
        String questName = quest.getQuestName();
        int companyId = quest.getCompanyId();

        when(questRepository.existsQuestByQuestNameAndCompanyId(questName, companyId)).thenReturn(Boolean.TRUE);
        boolean existsQuestName = questService.existQuestNameByCompany(questName, companyId);

        assertThat(existsQuestName).isTrue();

        verify(questRepository).existsQuestByQuestNameAndCompanyId(questName, companyId);
    }

    @Test
    void existQuestByCompany() {
        Quest quest = getQuest();
        int companyId = quest.getCompanyId();

        when(questRepository.existsQuestByIdAndCompanyId(quest.getId(), companyId)).thenReturn(Boolean.TRUE);
        boolean existsQuest = questService.existQuestByCompany(quest, companyId);

        assertThat(existsQuest).isTrue();

        verify(questRepository).existsQuestByIdAndCompanyId(quest.getId(), companyId);
    }

    @Test
    void saveQuest_saveAccount() {
        Quest quest = getQuest();
        Account account = getAccountEmptyQuests();
        quest.setAccounts(List.of(account));
        questService.saveQuest(quest);

        verify(questRepository).save(quest);
        verify(accountRepository).save(account);
    }

    @Test
    void saveQuest_notSaveAccount() {
        Quest quest = getQuest();
        Account account = getAccountWithQuests();
        quest.setAccounts(List.of(account));
        questService.saveQuest(quest);

        verify(questRepository).save(quest);
        verify(accountRepository, never()).save(account);
    }

    @Test
    void deleteQuest_changeAccount() {
        Quest quest = getQuest();
        Account accountSpy = spy(getAccountEmptyQuests());
        Set<Quest> questsInAccountSpy = spy(new TreeSet<>());

        doReturn(questsInAccountSpy).when(accountSpy).getQuests();
        when(accountRepository.findAllByQuestIdOrderByName(quest.getId())).thenReturn(List.of(accountSpy));

        questService.deleteQuest(quest);

        verify(reservationRepository).deleteByQuestId(quest.getId());
        verify(accountRepository).findAllByQuestIdOrderByName(quest.getId());
        verify(questsInAccountSpy).remove(quest);
        verify(accountRepository).save(accountSpy);
        verify(questRepository).deleteById(quest.getId());
    }

    @Test
    void deleteQuest_noChangeAccount() {
        Quest quest = getQuest();
        Account accountSpy = spy(getAccountEmptyQuests());
        Set<Quest> questsInAccountSpy = spy(new TreeSet<>());

        lenient().when(accountSpy.getQuests()).thenReturn(questsInAccountSpy);
        when(accountRepository.findAllByQuestIdOrderByName(quest.getId())).thenReturn(new ArrayList<>());

        questService.deleteQuest(quest);

        verify(reservationRepository).deleteByQuestId(quest.getId());
        verify(accountRepository).findAllByQuestIdOrderByName(quest.getId());
        verify(questsInAccountSpy, never()).remove(quest);
        verify(accountRepository, never()).save(accountSpy);
        verify(questRepository).deleteById(quest.getId());
    }

    private Quest getQuest() {
       return Quest.builder()
                .id(1)
                .questName("Quest One")
                .companyId(1)
                .build();
    }

    private Account getAccountEmptyQuests() {
        return Account.builder()
                .id(1)
                .login("login")
                .quests(new TreeSet<>())
                .build();
    }

    private Account getAccountWithQuests() {
        return Account.builder()
                .id(1)
                .login("login")
                .quests(Set.of(getQuest()))
                .build();
    }
}