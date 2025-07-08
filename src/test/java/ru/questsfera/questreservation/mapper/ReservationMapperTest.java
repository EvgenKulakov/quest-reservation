package ru.questsfera.questreservation.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.questsfera.questreservation.model.dto.ReservationWithClient;
import ru.questsfera.questreservation.model.dto.Status;
import ru.questsfera.questreservation.model.entity.Client;
import ru.questsfera.questreservation.model.entity.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationMapperTest {

    private final ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);

    @Test
    void toEntity_success() {
        ReservationWithClient reservationWithClient = getReservationWithClient();
        Reservation actual = reservationMapper.toEntity(reservationWithClient);
        Reservation excepted = getReservation();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(excepted);
    }

    @Test
    void toEntity_returnNull() {
        Reservation actual = reservationMapper.toEntity(null);
        assertThat(actual).isNull();
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

    private Reservation getReservation() {
        return new Reservation(
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
                1,
                1,
                "Hi",
                null,
                "default"
        );
    }
}