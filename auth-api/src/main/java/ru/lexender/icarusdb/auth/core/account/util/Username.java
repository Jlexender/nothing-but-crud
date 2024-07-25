package ru.lexender.icarusdb.auth.core.account.util;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record Username(
        @NotNull
        @Size(min = 4, max = 32)
        @Pattern(
                regexp = "^[A-Za-z][A-Za-z0-9_]*$",
                message = "must start with a letter and can only contain letters, numbers, and underscores"
        )
        String value
) {
}
