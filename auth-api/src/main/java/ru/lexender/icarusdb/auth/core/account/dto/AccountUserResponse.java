package ru.lexender.icarusdb.auth.core.account.dto;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.Collection;

public record AccountUserResponse(
        String username,
        String name,
        String surname,
        LocalDate birthDate,
        ByteBuffer avatar,
        Collection<String> authorities,
        LocalDate lockUntil
) {
}
