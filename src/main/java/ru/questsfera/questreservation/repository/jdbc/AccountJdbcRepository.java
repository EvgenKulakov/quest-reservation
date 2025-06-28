package ru.questsfera.questreservation.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.questsfera.questreservation.model.entity.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AccountJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * @return список отсортированных объектов {@link Account}:
     *         <ul>
     *           <li>для владельца (ROLE_OWNER) – все аккаунты компании, кроме собственного;</li>
     *           <li>для остальных – только аккаунты с ролью USER, кроме собственного.</li>
     *         </ul>
     */
    public List<Account> findOwnAccountsByMyAccountOrderByName(Account myAccount) {
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

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> accountResultSetMapping(rs));
    }

    public List<Account> findAllAccountsInCompanyByOwnAccountId(Integer accountId) {
        String sql =
                "SELECT * FROM accounts ac " +
                        "WHERE ac.company_id = " +
                        "(SELECT a.company_id FROM accounts a WHERE a.id = :accountId) " +
                        "ORDER BY ac.first_name, ac.last_name";

        Map<String, Object> params = Map.of("accountId", accountId);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> accountResultSetMapping(rs));
    }

    private Account accountResultSetMapping(ResultSet rs) throws SQLException {
        return new Account(
                rs.getObject("id", Integer.class),
                rs.getString("login"),
                rs.getString("password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                Account.Role.valueOf(rs.getString("role")),
                rs.getObject("company_id", Integer.class),
                null
        );
    }
}
