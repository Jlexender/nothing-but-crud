package ru.lexender.icarusdb.auth.core.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AccountLockRequest(
        @NotNull
        @JsonFormat(pattern = "dd-MM-yyyy")
        @JsonProperty("lock_until")
        LocalDate lockUntil
) {
}
