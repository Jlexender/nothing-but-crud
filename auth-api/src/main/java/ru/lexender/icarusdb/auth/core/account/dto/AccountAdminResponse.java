package ru.lexender.icarusdb.auth.core.account.dto;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

public record AccountAdminResponse(
        String username,
        String email,
        String name,
        String surname,
        LocalDate birthDate,
        ByteBuffer avatar,
        LocalDateTime createdAt,
        Collection<String> authorities,
        LocalDate lockUntil
) {
}
