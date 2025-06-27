package ru.questsfera.questreservation.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.model.entity.Quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.questsfera.questreservation.model.entity.Account.Role.*;

@Component
public class DomainPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(
            Authentication authentication,
            Object targetDomainObject,
            Object permission
    ) {
        AccountUserDetails principal = (AccountUserDetails) authentication.getPrincipal();
        PermissionType permissionType = PermissionType.valueOf((String) permission);

        return switch (targetDomainObject) {
            case Account account -> hasCommonCompanyByAccount(principal, account, permissionType);
            case Quest quest -> hasOwnerQuest(principal, quest, permissionType);
            default -> throw new IllegalStateException("Unexpected value: " + targetDomainObject);
        };
    }

    @Override
    public boolean hasPermission(
            Authentication authentication,
            Serializable targetId,
            String targetType,
            Object permission
    ) {
        AccountUserDetails principal = (AccountUserDetails) authentication.getPrincipal();
        PermissionType permissionType = PermissionType.valueOf((String) permission);

        return switch (TargetType.valueOf(targetType)) {
            case LIST_ACCOUNTS -> hasCommonCompanyByListAccounts(principal, (ArrayList<Account>) targetId, permissionType);
            case LIST_QUESTS -> hasOwnerListQuests(principal, (Set<Quest>) targetId, permissionType);
        };
    }

    private boolean hasCommonCompanyByAccount(
            AccountUserDetails principal,
            Account targetAccount,
            PermissionType permissionType
    ) {
        boolean isCommonCompany = principal.getCompanyId().equals(targetAccount.getCompanyId());

        boolean isAllowedRole = switch (permissionType) {
            case ONLY_OWNER ->
                    principal.getRole() == ROLE_OWNER && targetAccount.getRole() != ROLE_OWNER;
            case GRADATION_ROLES ->
                    principal.getRole() == ROLE_OWNER && targetAccount.getRole() != ROLE_OWNER ||
                            principal.getRole() == ROLE_ADMIN && targetAccount.getRole() == ROLE_USER;
            default -> throw new IllegalStateException("Unexpected value: " + permissionType);
        };

        return isCommonCompany && isAllowedRole;
    }

    private boolean hasCommonCompanyByListAccounts(
            AccountUserDetails principal,
            ArrayList<Account> listAccounts,
            PermissionType permissionType
    ) {
        boolean isCommonCompany = listAccounts
                .stream()
                .map(Account::getCompanyId)
                .allMatch(principal.getCompanyId()::equals);

        boolean isAllowedRole = switch (permissionType) {
            case ONLY_OWNER ->
                    principal.getRole() == ROLE_OWNER &&
                            listAccounts
                                    .stream()
                                    .filter(acc -> !principal.getId().equals(acc.getId()))
                                    .map(Account::getRole)
                                    .allMatch(role -> role != ROLE_OWNER);
            case GRADATION_ROLES ->
                    principal.getRole() == ROLE_OWNER &&
                            listAccounts
                                    .stream()
                                    .filter(acc -> !principal.getId().equals(acc.getId()))
                                    .map(Account::getRole)
                                    .allMatch(role -> role != ROLE_OWNER) ||
                            principal.getRole() == ROLE_ADMIN &&
                                    listAccounts
                                            .stream()
                                            .map(Account::getRole)
                                            .allMatch(role -> role == ROLE_USER);
            default -> throw new IllegalStateException("Unexpected value: " + permissionType);
        };

        return isCommonCompany && isAllowedRole;
    }

    private boolean hasOwnerQuest(
            AccountUserDetails principal,
            Quest quest,
            PermissionType permissionType
    ) {
        boolean isOwnerQuest = principal.getQuestIds().contains(quest.getId());

        boolean isAllowedRole = switch (permissionType) {
            case ONLY_OWNER -> principal.getRole() == ROLE_OWNER;
            case OWNER_AND_ADMIN -> principal.getRole() == ROLE_OWNER || principal.getRole() == ROLE_ADMIN;
            default -> throw new IllegalStateException("Unexpected value: " + permissionType);
        };

        return isOwnerQuest && isAllowedRole;
    }

    private boolean hasOwnerListQuests(
            AccountUserDetails principal,
            Set<Quest> targetQuests,
            PermissionType permissionType
    ) {
        boolean isOwnerQuests = principal.getQuestIds()
                .containsAll(targetQuests
                        .stream()
                        .map(Quest::getId)
                        .collect(Collectors.toSet()));

        boolean isAllowedRole = switch (permissionType) {
            case ONLY_OWNER -> principal.getRole() == ROLE_OWNER;
            case OWNER_AND_ADMIN -> principal.getRole() == ROLE_OWNER  || principal.getRole() == ROLE_ADMIN;
            default -> throw new IllegalStateException("Unexpected value: " + permissionType);
        };

        return isOwnerQuests && isAllowedRole;
    }

    public enum TargetType {
        LIST_ACCOUNTS,
        LIST_QUESTS
    }

    public enum PermissionType {
        ONLY_OWNER,
        OWNER_AND_ADMIN,
        GRADATION_ROLES,
        ANY
    }
}
