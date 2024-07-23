package ru.lexender.icarusdb.auth.core.account.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.model.Account;
import ru.lexender.icarusdb.auth.core.account.repository.AccountRepository;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class AccountService {
    AccountRepository accountRepository;

    public Mono<Account> save(Account account) {
        return accountRepository.save(account);
    }

    public Mono<Void> setLockByUsername(String username, boolean lock) {
        return accountRepository.findByUsername(username)
                .flatMap(account -> {
                    account.setLocked(lock);
                    return accountRepository.save(account);
                }).then();
    }


    public Mono<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
