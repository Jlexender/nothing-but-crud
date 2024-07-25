package ru.lexender.icarusdb.auth.core.account.model;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum AccountRole {
    ROLE_USER, ROLE_ADMIN, ROLE_STAFF
}
