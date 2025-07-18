package ru.questsfera.questreservation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.model.entity.Account;
import ru.questsfera.questreservation.service.account.AccountService;

@Component
@RequiredArgsConstructor
public class AccountConverter implements Converter<String, Account> {

    private final AccountService accountService;

    @Override
    public Account convert(String source) {
        Integer id = Integer.parseInt(source);
        return accountService.findAccountById(id);
    }
}
