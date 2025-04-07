package ru.questsfera.questreservation.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.questsfera.questreservation.entity.Account;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Account> findAllByAccountOrderByName(Account myAccount) {
        String sql = "SELECT * FROM accounts ac WHERE ac.company_id = :companyId AND ac.id != :myAccountId ";
        if (myAccount.getRole() != Account.Role.ROLE_OWNER) {
            sql += "AND ac.role = 'ROLE_USER' ";
        }
        sql += "ORDER BY ac.first_name, ac.last_name";

        Map<String, Object> params = Map.of("companyId", myAccount.getCompanyId(), "myAccountId", myAccount.getId());

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            String roleStr = rs.getString("role");
            Account.Role role = roleStr != null ? Account.Role.valueOf(roleStr) : null;

            return new Account(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    role,
                    rs.getInt("company_id"),
                    null // TODO optimisation quests
            );
        });
    }
}
