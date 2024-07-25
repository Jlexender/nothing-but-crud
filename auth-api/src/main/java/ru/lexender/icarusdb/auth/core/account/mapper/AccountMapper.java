package ru.lexender.icarusdb.auth.core.account.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;
import ru.lexender.icarusdb.auth.core.account.dto.AccountRequest;
import ru.lexender.icarusdb.auth.core.account.dto.AccountResponse;
import ru.lexender.icarusdb.auth.core.account.model.Account;
import ru.lexender.icarusdb.auth.core.account.util.Email;
import ru.lexender.icarusdb.auth.core.account.util.Password;
import ru.lexender.icarusdb.auth.core.account.util.Username;

@Service
@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "username", source = "username.value")
    @Mapping(target = "password", source = "password.value")
    @Mapping(target = "email", source = "email.value")
    Account accountRequestToAccount(AccountRequest accountRequest);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "email", source = "email")
    AccountResponse accountToAccountResponse(Account account);

    default String mapUsername(Username username) {
        return username.value();
    }

    default String mapPassword(Password password) {
        return password.value();
    }

    default String mapEmail(Email email) {
        return email.value();
    }
}
