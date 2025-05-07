package ru.questsfera.questreservation.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.questsfera.questreservation.model.dto.AccountDTO;
import ru.questsfera.questreservation.model.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "company.id", target = "companyId")
    Account toEntity(AccountDTO accountDTO);
}
