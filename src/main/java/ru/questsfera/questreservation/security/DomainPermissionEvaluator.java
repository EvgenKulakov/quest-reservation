package ru.questsfera.questreservation.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;

import java.io.Serializable;

import static ru.questsfera.questreservation.model.entity.Account.Role.*;

@Component
public class DomainPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        AccountUserDetails principal = (AccountUserDetails) authentication.getPrincipal();

        return switch (targetDomainObject) {
            case Account account -> hasPermissionAccount(principal, account);
            case Quest quest -> hasPermissionQuest(principal, quest, permission);
            default -> throw new IllegalStateException("Unexpected value: " + targetDomainObject);
        };
    }

    private boolean hasPermissionAccount(AccountUserDetails principal, Account targetAccount) {
        boolean isCommonCompany = principal.getCompanyId().equals(targetAccount.getCompanyId());
        boolean isAllowedRole = principal.getRole() == ROLE_OWNER || targetAccount.getRole() == ROLE_USER;
        return isCommonCompany && isAllowedRole;
    }

    private boolean hasPermissionQuest(AccountUserDetails principal, Quest quest, Object permission) {
        Account.Role role = valueOf((String) permission);

        boolean isCommonCompany = principal.getCompanyId().equals(quest.getCompanyId());

        boolean isAllowedRole = switch (role) {
            case ROLE_OWNER -> principal.getRole() == ROLE_OWNER;
            case ROLE_ADMIN -> principal.getRole() == ROLE_OWNER || principal.getRole() == ROLE_ADMIN;
            default -> throw new IllegalStateException("Unexpected value: " + role);
        };

        return isCommonCompany && isAllowedRole;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
