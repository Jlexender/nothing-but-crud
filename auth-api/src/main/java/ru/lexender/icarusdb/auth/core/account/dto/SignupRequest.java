package ru.lexender.icarusdb.auth.core.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignupRequest(
        @Valid
        Username username,
        @Valid
        Password password,
        @Valid @JsonProperty("password_repeat")
        Password passwordRepeat,
        @NotNull(message = "Email must not be null") @Email(message = "Email must be a valid email address")
        String email
) {
}
