package ru.lexender.icarusdb.auth.core.account.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.model.Account;
import ru.lexender.icarusdb.auth.core.account.repository.AccountRepository;

import java.time.LocalDate;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class AccountService {
    AccountRepository accountRepository;

    @Transactional
    public Mono<Account> save(Account account) {
        return accountRepository.save(account);
    }

    @Transactional
    public Mono<Void> lockByUsername(String username, LocalDate lockUntil) {
        return accountRepository.findById(username)
                .flatMap(account -> {
                    account.setLockUntil(lockUntil);
                    return accountRepository.save(account);
                }).then();
    }

    @Transactional
    public Mono<Void> unlockByUsername(String username) {
        return accountRepository.findById(username)
                .flatMap(account -> {
                    account.setLockUntil(LocalDate.now().minusDays(1));
                    return accountRepository.save(account);
                }).then();
    }

    @Transactional(readOnly = true)
    public Mono<Long> count() {
        return accountRepository.count();
    }

    @Transactional(readOnly = true)
    public Mono<Boolean> existsByUsername(String username) {
        return accountRepository.existsById(username);
    }

    @Transactional(readOnly = true)
    public Mono<Account> findByUsername(String username) {
        return accountRepository.findById(username);
    }
}
