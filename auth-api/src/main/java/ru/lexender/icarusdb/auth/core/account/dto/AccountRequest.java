package ru.lexender.icarusdb.auth.core.account.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.lexender.icarusdb.auth.core.account.util.Email;
import ru.lexender.icarusdb.auth.core.account.util.Password;
import ru.lexender.icarusdb.auth.core.account.util.Username;

public record AccountRequest(
        @NotNull @Valid
        Username username,
        @NotNull
        Password password,
        @NotNull @Valid
        Email email
) {
}
