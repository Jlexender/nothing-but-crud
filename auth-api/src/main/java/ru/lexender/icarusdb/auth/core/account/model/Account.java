package ru.lexender.icarusdb.auth.core.account.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Account {
    @PrimaryKey
    UUID id;

    @Size(min = 5, max = 32)
    @NotNull
    String username;

    @Size(min = 8, max = 64)
    @NotNull
    String password;

    Set<AccountAuthorities> accountAuthorities;

    boolean locked;
}
