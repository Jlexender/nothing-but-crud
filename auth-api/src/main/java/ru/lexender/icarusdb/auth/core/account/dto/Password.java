package ru.lexender.icarusdb.auth.core.account.dto;

import jakarta.validation.constraints.Size;

public record Password(
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        String value
) {
}
