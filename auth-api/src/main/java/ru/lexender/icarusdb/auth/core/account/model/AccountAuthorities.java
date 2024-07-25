package ru.lexender.icarusdb.auth.core.account.model;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum AccountAuthorities {
    ROLE_USER, ROLE_ADMIN, ROLE_STAFF
}
