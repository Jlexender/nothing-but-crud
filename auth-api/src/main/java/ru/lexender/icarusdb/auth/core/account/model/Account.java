package ru.lexender.icarusdb.auth.core.account.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Account {
    @PrimaryKey
    UUID id;

    @Size(min = 5, max = 32, message = "Username must be between 5 and 32 characters")
    @NotNull(message = "Username must not be null")
    String username;

    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @NotNull(message = "Password must not be null")
    String password;

    @NotNull(message = "Email must not be null")
    @Email(message = "Email must be a valid email address")
    String email;

    @Builder.Default
    Set<AccountAuthorities> accountAuthorities = Set.of(AccountAuthorities.ROLE_USER);

    @Builder.Default
    boolean locked = false;
}
