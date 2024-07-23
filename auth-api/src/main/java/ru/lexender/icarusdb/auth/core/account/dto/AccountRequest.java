package ru.lexender.icarusdb.auth.core.account.dto;

import jakarta.validation.constraints.Size;

public record AccountRequest(
        @Size(min = 5, max = 32) String username,
        @Size(min = 8, max = 64) String password
) {
}
