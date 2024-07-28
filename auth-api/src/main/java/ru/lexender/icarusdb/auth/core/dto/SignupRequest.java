package ru.lexender.icarusdb.auth.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ru.lexender.icarusdb.auth.core.account.util.validation.Match;

import java.time.LocalDate;

@Builder
@Match(firstField = "password", secondField = "passwordConfirm")
public record SignupRequest(
        @NotNull
        @Size(min = 4, max = 32)
        @Pattern(
                regexp = "^[A-Za-z][A-Za-z0-9_]*$",
                message = "must start with a letter and can only contain English letters, numbers, and underscores"
        )
        String username,

        @NotNull
        @Size(min = 8, max = 32)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]*$",
                message = "must contain at least one lowercase letter, one uppercase letter, and one digit"
        )
        String password,

        @NotNull
        @Valid
        @Size(min = 8, max = 32)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]*$",
                message = "must contain at least one lowercase letter, one uppercase letter, and one digit"
        )
        @JsonProperty("password_confirm")
        String passwordConfirm,

        @NotNull
        @Email
        String email,

        @NotBlank
        String name,
        String surname,

        @JsonFormat(pattern = "dd-MM-yyyy")
        @JsonProperty("birth_date")
        LocalDate birthDate
) {
}
