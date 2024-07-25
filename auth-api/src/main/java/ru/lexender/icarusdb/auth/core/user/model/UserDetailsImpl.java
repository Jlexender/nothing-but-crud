package ru.lexender.icarusdb.auth.core.user.model;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.lexender.icarusdb.auth.core.account.model.Account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
    Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getAccountAuthorities()
                .stream()
                .map(o -> new SimpleGrantedAuthority(o.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.getLockUntil().isBefore(LocalDate.now());
    }
}
