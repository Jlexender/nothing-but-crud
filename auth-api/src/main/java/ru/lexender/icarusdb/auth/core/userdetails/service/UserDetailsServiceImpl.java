package ru.lexender.icarusdb.auth.core.userdetails.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.service.AccountService;
import ru.lexender.icarusdb.auth.core.userdetails.model.UserDetailsImpl;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {
    AccountService accountService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return accountService.findByUsername(username)
                .map(UserDetailsImpl::new);
    }
}
