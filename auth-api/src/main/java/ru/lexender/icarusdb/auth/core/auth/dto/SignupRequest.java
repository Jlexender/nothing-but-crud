package ru.lexender.icarusdb.auth.core.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import ru.lexender.icarusdb.auth.core.account.util.Email;
import ru.lexender.icarusdb.auth.core.account.util.Password;
import ru.lexender.icarusdb.auth.core.account.util.Username;

public record SignupRequest(
        @Valid
        Username username,
        @Valid
        Password password,
        @Valid @JsonProperty("password_repeat")
        Password passwordRepeat,
        @Valid
        Email email
) {
}
