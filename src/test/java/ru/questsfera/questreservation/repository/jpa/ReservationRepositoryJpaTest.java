package ru.questsfera.questreservation.repository.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.questsfera.questreservation.config.Profile;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(Profile.H2_TEST)
@Sql(scripts = {"classpath:common_test_data.sql"})
class ReservationRepositoryJpaTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    void existsByQuestId() {
        boolean existsReservations = reservationRepository.existsByQuestId(1);
        boolean notExistsReservations = reservationRepository.existsByQuestId(100);
        assertThat(existsReservations).isTrue();
        assertThat(notExistsReservations).isFalse();
    }

    @Test
    void deleteByQuestId() {
        reservationRepository.deleteByQuestId(1);
        long afterDeleteById1 = reservationRepository.count();
        assertThat(afterDeleteById1).isEqualTo(2L);

        reservationRepository.deleteByQuestId(2);
        long afterDeleteById2 = reservationRepository.count();
        assertThat(afterDeleteById2).isEqualTo(0);
    }

    @Test
    void existsByQuestIdAndDateReserveAndTimeReserve() {
        boolean existsReservations = reservationRepository.existsByQuestIdAndDateReserveAndTimeReserve(
                1, LocalDate.parse("2025-04-21"), LocalTime.parse("16:00:00")
        );
        boolean notExistsReservations1 = reservationRepository.existsByQuestIdAndDateReserveAndTimeReserve(
                100, LocalDate.parse("2025-04-21"), LocalTime.parse("16:00:00")
        );
        boolean notExistsReservations2 = reservationRepository.existsByQuestIdAndDateReserveAndTimeReserve(
                1, LocalDate.parse("2099-04-21"), LocalTime.parse("16:00:00")
        );
        boolean notExistsReservations3 = reservationRepository.existsByQuestIdAndDateReserveAndTimeReserve(
                1, LocalDate.parse("2025-04-21"), LocalTime.parse("23:00:00")
        );

        assertThat(existsReservations).isTrue();
        assertThat(notExistsReservations1).isFalse();
        assertThat(notExistsReservations2).isFalse();
        assertThat(notExistsReservations3).isFalse();
    }
}