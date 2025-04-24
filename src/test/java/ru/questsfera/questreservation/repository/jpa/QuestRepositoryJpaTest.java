package ru.questsfera.questreservation.repository.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.config.Profile;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(Profile.H2_TEST)
@Sql(scripts = {"classpath:common_test_data.sql"})
class QuestRepositoryJpaTest {

    @Autowired
    QuestRepository questRepository;

    @Test
    void existsQuestByQuestNameAndCompanyId() {
        boolean existsQuest = questRepository.existsQuestByQuestNameAndCompanyId("Quest One", 1);
        boolean notExistsQuest1 = questRepository.existsQuestByQuestNameAndCompanyId("No quest", 1);
        boolean notExistsQuest2 = questRepository.existsQuestByQuestNameAndCompanyId("Quest One", 100);
        boolean notExistsQuest3 = questRepository.existsQuestByQuestNameAndCompanyId("No quest", 100);

        assertThat(existsQuest).isTrue();
        assertThat(notExistsQuest1).isFalse();
        assertThat(notExistsQuest2).isFalse();
        assertThat(notExistsQuest3).isFalse();
    }

    @Test
    void existsQuestByIdAndCompanyId() {
        boolean existsQuest = questRepository.existsQuestByIdAndCompanyId(1, 1);
        boolean notExistsQuest1 = questRepository.existsQuestByIdAndCompanyId(100, 1);
        boolean notExistsQuest2 = questRepository.existsQuestByIdAndCompanyId(1, 100);
        boolean notExistsQuest3 = questRepository.existsQuestByIdAndCompanyId(100, 100);

        assertThat(existsQuest).isTrue();
        assertThat(notExistsQuest1).isFalse();
        assertThat(notExistsQuest2).isFalse();
        assertThat(notExistsQuest3).isFalse();
    }

    @Test
    void findAllByCompanyIdOrderByQuestName() {
        List<Quest> actualQuests = questRepository.findAllByCompanyIdOrderByQuestName(1);
        List<Quest> exceptedQuests = getQuestsWithoutAccounts();

        assertThat(actualQuests)
                .usingRecursiveComparison()
                .ignoringFields("accounts")
                .isEqualTo(exceptedQuests);
    }

    @Test
    void findAllByAccount_login() {
        Set<Quest> actualQuests = questRepository.findAllByAccount_login("admin@gmail.com");
        Set<Quest> exceptedQuests = Set.copyOf(getQuestsWithoutAccounts());

        assertThat(actualQuests)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields("accounts")
                .isEqualTo(exceptedQuests);
    }

    private List<Quest> getQuestsWithoutAccounts() {
        String slotListQuestOne = """
               {
                 "monday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "tuesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "wednesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "thursday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "friday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "saturday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
                 "sunday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ]
               }""";

        String slotListQuestTwo = """
                {
                  "monday" : [ {"time" : "12:30","price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "tuesday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "wednesday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "thursday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "friday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "saturday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
                  "sunday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ]
                }""";

        Quest questOne = Quest.builder()
                .id(1)
                .questName("Quest One")
                .minPersons(1)
                .maxPersons(6)
                .autoBlock(LocalTime.MIN)
                .slotList(slotListQuestOne)
                .companyId(1)
                .statuses("NEW_RESERVE,CANCEL,CONFIRMED,NOT_COME,COMPLETED")
                .synchronizedQuests(new HashSet<>())
                .build();

        Quest questTwo = Quest.builder()
                .id(2)
                .questName("Quest Two")
                .minPersons(1)
                .maxPersons(5)
                .autoBlock(LocalTime.MIN)
                .slotList(slotListQuestTwo)
                .companyId(1)
                .statuses("NEW_RESERVE,CANCEL,CONFIRMED,NOT_COME,COMPLETED")
                .synchronizedQuests(new HashSet<>())
                .build();

        return List.of(questOne, questTwo);
    }
}