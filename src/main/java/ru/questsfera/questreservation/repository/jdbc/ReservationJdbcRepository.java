package ru.questsfera.questreservation.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.model.dto.ReservationDTO;
import ru.questsfera.questreservation.model.dto.StatusType;
import ru.questsfera.questreservation.model.entity.Client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ReservationJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ReservationDTO findReservationDtoById(Long id) {
        String sql =
                "SELECT res.id AS reservation_id, cl.id AS client_id, res.*, cl.* " +
                        "FROM (SELECT * FROM reservations r WHERE r.id = :id) AS res " +
                        "LEFT JOIN clients cl ON res.client_id = cl.id";
        Map<String, Object> params = Map.of("id", id);

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
            Client client = clientResultSetMapping(rs);
            return reservationDtoResultSetMapping(rs, client);
        });
    }

    public List<ReservationDTO> findActiveByQuestIdsAndDate(Collection<Integer> questIds, LocalDate dateReserve) {
        String sql =
                "SELECT res.id, res.time_reserve, res.quest_id, res.status_type " +
                        "FROM reservations res " +
                        "WHERE res.date_reserve = :dateReserve " +
                        "AND res.quest_id IN (:questIds) " +
                        "AND res.status_type != 'CANCEL'";
        Map<String, Object> params = Map.of("questIds", questIds, "dateReserve", dateReserve);

        return jdbcTemplate.query(sql, params, (rs, rowNum) ->
                ReservationDTO.builder()
                        .id(rs.getObject("id", Long.class))
                        .timeReserve(rs.getObject("time_reserve", LocalTime.class))
                        .questId(rs.getObject("quest_id", Integer.class))
                        .statusType(StatusType.valueOf(rs.getString("status_type")))
                        .build());
    }

    private Client clientResultSetMapping(ResultSet rs) throws SQLException {
        if (rs.getObject("client_id", Integer.class) == null) return null;

        return new Client(
                rs.getObject("client_id", Integer.class),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("comments"),
                rs.getObject("blacklist_id", Integer.class),
                rs.getObject("company_id", Integer.class)
        );
    }

    private ReservationDTO reservationDtoResultSetMapping(ResultSet rs, Client client) throws SQLException {
        return new ReservationDTO(
                rs.getObject("reservation_id", Long.class),
                rs.getObject("date_reserve", LocalDate.class),
                rs.getObject("time_reserve", LocalTime.class),
                rs.getObject("time_created", LocalDateTime.class),
                rs.getObject("time_last_change", LocalDateTime.class),
                rs.getObject("changed_slot_time", LocalTime.class),
                rs.getObject("quest_id", Integer.class),
                StatusType.valueOf(rs.getString("status_type")),
                rs.getString("source_reserve"),
                rs.getBigDecimal("price"),
                rs.getBigDecimal("changed_price"),
                client,
                rs.getObject("count_persons", Integer.class),
                rs.getString("admin_comment"),
                rs.getString("client_comment"),
                rs.getString("history_messages")
        );
    }
}
