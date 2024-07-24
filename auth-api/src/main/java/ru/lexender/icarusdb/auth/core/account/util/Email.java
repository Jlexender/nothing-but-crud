package ru.lexender.icarusdb.auth.core.account.util;

import jakarta.validation.constraints.NotNull;

public record Email(
        @NotNull(message = "Email must not be null")
        @jakarta.validation.constraints.Email(message = "Email must be a valid email address")
        String value
) {
}
