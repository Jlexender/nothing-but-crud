package ru.lexender.icarusdb.auth.core.account.dto;

import ru.lexender.icarusdb.auth.core.account.model.AccountRole;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AccountAdminResponse(
        String username,
        String email,
        String name,
        String surname,
        LocalDate birthDate,
        ByteBuffer avatar,
        LocalDateTime created,
        AccountRole role,
        LocalDate lockUntil
) {
}
