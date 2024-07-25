package ru.lexender.icarusdb.auth.core.account.util;

import jakarta.validation.constraints.NotNull;

public record Email(
        @NotNull
        @jakarta.validation.constraints.Email
        String value
) {
}
