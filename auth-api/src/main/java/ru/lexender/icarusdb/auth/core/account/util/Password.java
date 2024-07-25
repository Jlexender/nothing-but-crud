package ru.lexender.icarusdb.auth.core.account.util;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record Password(
        @NotNull
        @Size(min = 8, max = 64)
        String value
) {
}
