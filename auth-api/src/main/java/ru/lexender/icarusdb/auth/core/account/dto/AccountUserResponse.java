package ru.lexender.icarusdb.auth.core.account.dto;

import ru.lexender.icarusdb.auth.core.account.model.AccountRole;

import java.nio.ByteBuffer;
import java.time.LocalDate;

public record AccountUserResponse(
        String username,
        String name,
        String surname,
        LocalDate birthDate,
        ByteBuffer avatar,
        AccountRole role,
        LocalDate lockUntil
) {
}