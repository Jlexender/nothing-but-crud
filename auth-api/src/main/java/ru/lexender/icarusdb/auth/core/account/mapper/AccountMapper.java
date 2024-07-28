package ru.lexender.icarusdb.auth.core.account.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import ru.lexender.icarusdb.auth.core.account.dto.AccountAdminResponse;
import ru.lexender.icarusdb.auth.core.account.dto.AccountCreationRequest;
import ru.lexender.icarusdb.auth.core.account.dto.AccountUserResponse;
import ru.lexender.icarusdb.auth.core.account.model.Account;

@Service
@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account accountCreationRequestToAccount(AccountCreationRequest request);

    AccountUserResponse accountToAccountUserResponse(Account account);

    AccountAdminResponse accountToAccountAdminResponse(Account account);
}
