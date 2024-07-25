package ru.lexender.icarusdb.auth.core.account.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import ru.lexender.icarusdb.auth.core.account.dto.AccountAdminResponse;
import ru.lexender.icarusdb.auth.core.account.dto.AccountCreationRequest;
import ru.lexender.icarusdb.auth.core.account.dto.AccountUserResponse;
import ru.lexender.icarusdb.auth.core.account.model.Account;
import ru.lexender.icarusdb.auth.core.dto.SignupRequest;

@Service
@Mapper(componentModel = "spring")
public interface RequestMapper {
    AccountCreationRequest signupRequestToAccountCreationRequest(SignupRequest request);
}

