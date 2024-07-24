package ru.lexender.icarusdb.auth.core.account.util;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record Username(
        @Size(min = 5, max = 32, message = "Username must be between 5 and 32 characters")
        @Pattern(
                regexp = "^[A-Za-z][A-Za-z0-9_]*$",
                message = "Username must start with a letter and can only contain letters, numbers, and underscores"
        )
        String value
) {
}
