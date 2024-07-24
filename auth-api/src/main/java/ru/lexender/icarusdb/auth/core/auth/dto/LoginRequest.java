package ru.lexender.icarusdb.auth.core.auth.dto;

import jakarta.validation.Valid;
import ru.lexender.icarusdb.auth.core.account.util.Password;
import ru.lexender.icarusdb.auth.core.account.util.Username;

public record LoginRequest(
        @Valid Username username,
        @Valid Password password
) {
}
