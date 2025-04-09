package ru.questsfera.questreservation.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.entity.Account;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AccountJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Account> findAllByAccountOrderByName(Account myAccount) {
        String sql =
                "SELECT * FROM accounts ac " +
                        "WHERE ac.company_id = :companyId " +
                        "AND ac.id != :myAccountId " +
                        "AND (:myAccRole = 'ROLE_OWNER' OR ac.role = 'ROLE_USER') " +
                        "ORDER BY ac.first_name, ac.last_name";

        Map<String, Object> params = Map.of(
                "companyId", myAccount.getCompanyId(),
                "myAccountId", myAccount.getId(),
                "myAccRole", myAccount.getRole().name()
        );

        return jdbcTemplate.query(sql, params, (rs, rowNum) ->
                new Account(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        Account.Role.valueOf(rs.getString("role")),
                        rs.getInt("company_id"),
                        null
                ));
    }
}
