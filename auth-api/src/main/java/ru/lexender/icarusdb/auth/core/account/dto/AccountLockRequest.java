package ru.lexender.icarusdb.auth.core.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record AccountLockRequest(
        @NotNull
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate lockUntil
) {
}
