package ru.questsfera.questreservation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.questsfera.questreservation.model.dto.AccountCreateForm;
import ru.questsfera.questreservation.model.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "quests", ignore = true)
    @Mapping(source = "company.id", target = "companyId")
    Account toEntity(AccountCreateForm accountCreateForm);
}
