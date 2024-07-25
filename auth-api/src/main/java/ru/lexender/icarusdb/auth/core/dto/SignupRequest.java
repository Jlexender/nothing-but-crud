package ru.lexender.icarusdb.auth.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.lexender.icarusdb.auth.core.account.util.Email;
import ru.lexender.icarusdb.auth.core.account.util.Password;
import ru.lexender.icarusdb.auth.core.account.util.Username;
import ru.lexender.icarusdb.auth.core.account.util.validation.Match;

@Match(firstField = "password", secondField = "passwordConfirm")
public record SignupRequest(
        @NotNull @Valid
        Username username,
        @NotNull @Valid
        Password password,
        @NotNull @Valid @JsonProperty("password_confirm")
        Password passwordConfirm,
        @NotNull @Valid
        Email email
) {
}
