package ru.questsfera.questreservation.repository.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import ru.questsfera.questreservation.model.dto.ReservationWithClient;
import ru.questsfera.questreservation.model.dto.Status;
import ru.questsfera.questreservation.model.entity.Client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(ReservationJdbcRepository.class)
@Sql(scripts = {"classpath:common_test_data.sql"})
class ReservationJdbcRepositoryTest {

    @Autowired
    ReservationJdbcRepository reservationJdbcRepository;

    @Test
    void findReservationWithClientById_success() {
        ReservationWithClient actualResDto = reservationJdbcRepository.findReservationWithClientById(1L);
        ReservationWithClient exceptedResDto = getReservationWithClient();
        assertThat(actualResDto)
                .usingRecursiveComparison()
                .isEqualTo(exceptedResDto);

        ReservationWithClient actualBlockResDto = reservationJdbcRepository.findReservationWithClientById(7L);
        ReservationWithClient exceptedBlockResDto = getBlockReservationWIthClient();
        assertThat(actualBlockResDto)
                .usingRecursiveComparison()
                .isEqualTo(exceptedBlockResDto);
    }

    @Test
    void findReservationWithClientById_failure() {
        assertThatThrownBy(() -> reservationJdbcRepository.findReservationWithClientById(1000L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void findActiveByQuestIdsAndDate_success() {
        List<ReservationWithClient> actualReservations = reservationJdbcRepository
                .findActiveByQuestIdsAndDate(List.of(1), LocalDate.parse("2025-04-21"));
        List<ReservationWithClient> exceptedReservations = getReservationsWIthClient();

        assertThat(actualReservations)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(exceptedReservations);
    }

    @Test
    void findActiveByQuestIdsAndDate_empty() {
        List<ReservationWithClient> emptyResultQuery1 = reservationJdbcRepository
                .findActiveByQuestIdsAndDate(List.of(100, 101), LocalDate.parse("2025-04-21"));
        List<ReservationWithClient> emptyResultQuery2 = reservationJdbcRepository
                .findActiveByQuestIdsAndDate(List.of(1), LocalDate.parse("2099-04-21"));
        List<ReservationWithClient> emptyResultQuery3 = reservationJdbcRepository
                .findActiveByQuestIdsAndDate(List.of(100, 101), LocalDate.parse("2099-04-21"));

        assertThat(emptyResultQuery1.isEmpty()).isTrue();
        assertThat(emptyResultQuery2.isEmpty()).isTrue();
        assertThat(emptyResultQuery3.isEmpty()).isTrue();
    }

    private ReservationWithClient getReservationWithClient() {
        return new ReservationWithClient(
                1L,
                LocalDate.parse("2025-04-21"),
                LocalTime.parse("16:00:00"),
                LocalDateTime.parse("2025-04-21T00:29:51.003024"),
                LocalDateTime.parse("2025-04-21T00:29:51.006025"),
                null,
                1,
                Status.CONFIRMED,
                "default",
                new BigDecimal("3000.00"),
                null,
                getClient(),
                1,
                "Hi",
                null,
                "default"
        );
    }

    private Client getClient() {
        return new Client(
                1,
                "TestName_client",
                "TestSurname_client",
                "+79995201511", "ee@email.com",
                null,
                null,
                1
        );
    }

    private ReservationWithClient getBlockReservationWIthClient() {
        return new ReservationWithClient(
                7L,
                LocalDate.parse("2025-04-21"),
                LocalTime.parse("16:30:00"),
                LocalDateTime.parse("2025-04-21T00:58:38.605035"),
                null,
                null,
                2,
                Status.BLOCK,
                "default",
                null,
                null,
                null,
                null,
                null,
                null,
                "default"
        );
    }

    private List<ReservationWithClient> getReservationsWIthClient() {
        ReservationWithClient res1 = ReservationWithClient.builder()
                .id(1L)
                .timeReserve(LocalTime.parse("16:00:00"))
                .questId(1)
                .status(Status.CONFIRMED)
                .build();

        ReservationWithClient res2 = ReservationWithClient.builder()
                .id(2L)
                .timeReserve(LocalTime.parse("17:00:00"))
                .questId(1)
                .status(Status.BLOCK)
                .build();

        return List.of(res1, res2);
    }
}