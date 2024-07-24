package ru.lexender.icarusdb.auth.core.account.dto;

import jakarta.validation.Valid;
import ru.lexender.icarusdb.auth.core.account.util.Email;
import ru.lexender.icarusdb.auth.core.account.util.Password;
import ru.lexender.icarusdb.auth.core.account.util.Username;

public record AccountRequest(
        @Valid Username username,
        @Valid Password password,
        @Valid Email email
) {
}
