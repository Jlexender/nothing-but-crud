package ru.lexender.icarusdb.auth.core.account.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Account {
    UUID id;

    String username;

    String password;

    Set<AccountAuthorities> accountAuthorities;

    boolean locked;
}
