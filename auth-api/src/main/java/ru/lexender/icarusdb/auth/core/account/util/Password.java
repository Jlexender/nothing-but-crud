package ru.lexender.icarusdb.auth.core.account.util;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record Password(
        @NotNull
        @Size(min = 8, max = 32)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]*$",
                message = "must contain at least one lowercase letter, one uppercase letter, and one digit"
        )
        String value
) {
}
