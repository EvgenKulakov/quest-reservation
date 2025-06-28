package ru.questsfera.questreservation.service.quest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;
import ru.questsfera.questreservation.repository.jpa.QuestRepository;
import ru.questsfera.questreservation.repository.jpa.ReservationRepository;
import ru.questsfera.questreservation.service.account.AccountService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestServiceTest {

    @Mock QuestRepository questRepository;
    @Mock ReservationRepository reservationRepository;
    @Mock AccountService accountService;
    @InjectMocks QuestService questService;

    @Test
    void getQuestsByCompany() {
        List<Quest> exceptedQuests = new ArrayList<>();
        when(questRepository.findAllByCompanyIdOrderByQuestName(anyInt())).thenReturn(exceptedQuests);
        List<Quest> actualQuests = questService.getQuestsByCompany(anyInt());

        assertThat(actualQuests).isSameAs(exceptedQuests);

        verify(questRepository).findAllByCompanyIdOrderByQuestName(anyInt());
    }

    @Test
    void findAllByAccount_id() {
        Set<Quest> exceptedQuests = new HashSet<>();
        when(questRepository.findAllByAccount_id(anyInt())).thenReturn(exceptedQuests);
        Set<Quest> actualQuests = questService.findAllByAccount_id(anyInt());

        assertThat(actualQuests).isSameAs(exceptedQuests);

        verify(questRepository).findAllByAccount_id(anyInt());
    }

    @Test
    void existQuestNameByCompany() {
        when(questRepository.existsQuestByQuestNameAndCompanyId(anyString(), anyInt())).thenReturn(Boolean.TRUE);
        boolean existsQuestName = questService.existQuestNameByCompany(anyString(), anyInt());

        assertThat(existsQuestName).isTrue();

        verify(questRepository).existsQuestByQuestNameAndCompanyId(anyString(), anyInt());
    }

    @Test
    void existQuestByCompany() {
        Quest quest = Mockito.mock(Quest.class);
        Integer questId = 1;
        Integer companyId = 1;

        when(quest.getId()).thenReturn(questId);
        when(questRepository.existsQuestByIdAndCompanyId(anyInt(), anyInt())).thenReturn(Boolean.TRUE);
        boolean existsQuest = questService.existQuestByCompany(quest, companyId);

        assertThat(existsQuest).isTrue();

        verify(questRepository).existsQuestByIdAndCompanyId(questId, companyId);
    }

    @Test
    void saveNewQuest_saveAccount() {
        Quest quest = Mockito.mock(Quest.class);
        Account account = Mockito.mock(Account.class);

        when(quest.getAccounts()).thenReturn(List.of(account));
        when(account.getQuests()).thenReturn(new HashSet<>());
        questService.saveNewQuest(quest, account);

        verify(questRepository).save(quest);
        verify(accountService).saveAccount(account);
        verify(accountService).updateCurrentAccount(account);
    }

    @Test
    void saveNewQuest_notSaveAccount() {
        Quest quest = Mockito.mock(Quest.class);
        Account account = Mockito.mock(Account.class);

        when(quest.getAccounts()).thenReturn(List.of(account));
        when(account.getQuests()).thenReturn(Set.of(quest));
        questService.saveNewQuest(quest, account);

        verify(questRepository).save(quest);
        verify(accountService, never()).saveAccount(account);
        verify(accountService, never()).updateCurrentAccount(account);
    }

    @Test
    void deleteQuest_changeAccount() {
        Quest quest = Mockito.mock(Quest.class);
        Integer questId = 1;
        Account account = Mockito.mock(Account.class);
        Set<Quest> questsInAccountSpy = spy(new TreeSet<>());

        when(quest.getId()).thenReturn(questId);
        when(account.getQuests()).thenReturn(questsInAccountSpy);
        when(accountService.getAccountsByQuestId(anyInt())).thenReturn(List.of(account));
        questService.deleteQuest(quest);

        verify(reservationRepository).deleteByQuestId(questId);
        verify(accountService).getAccountsByQuestId(questId);
        verify(questsInAccountSpy).remove(quest);
        verify(accountService).saveAccount(account);
        verify(questRepository).deleteById(questId);
    }

    @Test
    void deleteQuest_noChangeAccount() {
        Quest quest = Mockito.mock(Quest.class);
        Integer questId = 1;
        Account account = Mockito.mock(Account.class);

        when(quest.getId()).thenReturn(questId);
        when(accountService.getAccountsByQuestId(anyInt())).thenReturn(new ArrayList<>());
        questService.deleteQuest(quest);

        verify(reservationRepository).deleteByQuestId(questId);
        verify(accountService).getAccountsByQuestId(questId);
        verify(account, never()).getQuests();
        verify(accountService, never()).saveAccount(account);
        verify(questRepository).deleteById(questId);
    }
}