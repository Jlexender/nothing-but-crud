package ru.lexender.icarusdb.auth.core.account.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Account implements Serializable {
    @PrimaryKey
    @Size(min = 4, max = 32)
    @NotNull
    String username;

    @Size(min = 8, max = 64)
    @NotNull
    String password;

    @NotNull
    @Email
    String email;

    @NotBlank
    String name;

    String surname;

    @Column("birth_date")
    LocalDate birthDate;

    ByteBuffer avatar;

    @Builder.Default
    @NotNull
    @Column
    LocalDateTime created = LocalDateTime.now();

    @Column("role")
    @Builder.Default
    AccountRole role = AccountRole.ROLE_USER;

    @Column("lock_until")
    @Builder.Default
    LocalDate lockUntil = LocalDate.now().minusDays(1);
}
