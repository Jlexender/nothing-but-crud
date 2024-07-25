package ru.lexender.icarusdb.auth.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(
        @NotNull
        @Size(min = 8, max = 32)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]*$",
                message = "must contain at least one lowercase letter, one uppercase letter, and one digit"
        )
        @JsonProperty("old_password")
        String oldPassword,

        @NotNull
        @Size(min = 8, max = 32)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]*$",
                message = "must contain at least one lowercase letter, one uppercase letter, and one digit"
        )
        @JsonProperty("new_password")
        String newPassword,

        @NotNull
        @Size(min = 8, max = 32)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]*$",
                message = "must contain at least one lowercase letter, one uppercase letter, and one digit"
        )
        @JsonProperty("new_password_confirm")
        String newPasswordConfirm
) {
}
