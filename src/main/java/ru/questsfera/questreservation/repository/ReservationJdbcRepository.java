package ru.questsfera.questreservation.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.dto.ReservationDTO;
import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ReservationDTO findReservationDtoById(Long id) {
        String sql =
                "SELECT r.id AS reservation_id, cl.id AS client_id, r.*, cl.* " +
                        "FROM reservations r " +
                        "LEFT JOIN clients cl ON r.client_id = cl.id " +
                        "WHERE r.id = :id";
        Map<String, Object> params = Map.of("id", id);

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
            Client client = clientResultSetMapper(rs);
            return reservationDtoResultSetMapper(rs, client);
        });
    }

    public List<ReservationDTO> findActiveByQuestIdsAndDate(Collection<Integer> questIds, LocalDate dateReserve) {
        String sql =
                "SELECT res.id AS reservation_id, qu.id AS quest_id, cl.id AS client_id, res.*, cl.* " +
                        "FROM (SELECT * FROM reservations r WHERE r.date_reserve = :dateReserve) AS res " +
                        "INNER JOIN quests qu ON res.quest_id = qu.id " +
                        "LEFT JOIN clients cl ON res.client_id = cl.id " +
                        "WHERE res.quest_id IN (:questIds) " +
                        "AND res.status_type != 'CANCEL'";
        Map<String, Object> params = Map.of("questIds", questIds, "dateReserve", dateReserve);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Client client = clientResultSetMapper(rs);
            return reservationDtoResultSetMapper(rs, client);
        });
    }

    private Client clientResultSetMapper(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("client_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("comments"),
                null, // TODO blacklist optimisation
                null // TODO company optimisation
        );
    }

    private ReservationDTO reservationDtoResultSetMapper(ResultSet rs, Client client) throws SQLException {
        String statusTypeStr = rs.getString("status_type");
        StatusType statusType = statusTypeStr != null ? StatusType.valueOf(statusTypeStr) : null;

        return new ReservationDTO(
                rs.getLong("reservation_id"),
                rs.getObject("date_reserve", LocalDate.class),
                rs.getObject("time_reserve", LocalTime.class),
                rs.getObject("time_created", LocalDateTime.class),
                rs.getObject("time_last_change", LocalDateTime.class),
                rs.getObject("changed_slot_time", LocalTime.class),
                rs.getInt("quest_id"),
                statusType,
                rs.getString("source_reserve"),
                rs.getBigDecimal("price"),
                rs.getBigDecimal("changed_price"),
                client,
                rs.getInt("count_persons"),
                rs.getString("admin_comment"),
                rs.getString("client_comment"),
                rs.getString("history_messages")
        );
    }
}
