package ru.lexender.icarusdb.auth.core.account.dto;

import java.util.Collection;

public record AccountResponse(
        String username,
        String password,
        Collection<String> authorities,
        boolean locked
) {
}
