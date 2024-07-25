package ru.lexender.icarusdb.auth.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.lexender.icarusdb.auth.core.account.util.Password;
import ru.lexender.icarusdb.auth.core.account.util.Username;

public record LoginRequest(
        @NotNull @Valid
        Username username,
        @NotNull @Valid
        Password password
) {
}
