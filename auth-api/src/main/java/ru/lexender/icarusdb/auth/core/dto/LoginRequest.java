package ru.lexender.icarusdb.auth.core.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        String username,

        @NotNull
        String password
) {
}
