package ru.lexender.icarusdb.auth.core.account.log.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.log.model.AccountLog;
import ru.lexender.icarusdb.auth.core.account.log.repository.AccountLogRepository;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class AccountLogService {
    AccountLogRepository accountLogRepository;

    @Transactional
    public Mono<AccountLog> save(AccountLog accountLog) {
        return accountLogRepository.save(accountLog);
    }

    @Transactional(readOnly = true)
    public Mono<Boolean> existsByEmail(String email) {
        return accountLogRepository.existsById(email);
    }
}
