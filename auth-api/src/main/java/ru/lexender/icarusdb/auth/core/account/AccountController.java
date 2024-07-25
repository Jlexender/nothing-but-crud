package ru.lexender.icarusdb.auth.core.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.dto.AccountAdminResponse;
import ru.lexender.icarusdb.auth.core.account.dto.AccountCreationRequest;
import ru.lexender.icarusdb.auth.core.account.dto.AccountLockRequest;
import ru.lexender.icarusdb.auth.core.account.dto.AccountUserResponse;
import ru.lexender.icarusdb.auth.core.account.log.model.AccountLog;
import ru.lexender.icarusdb.auth.core.account.log.service.AccountLogService;
import ru.lexender.icarusdb.auth.core.account.mapper.AccountMapper;
import ru.lexender.icarusdb.auth.core.account.model.Account;
import ru.lexender.icarusdb.auth.core.account.model.AccountAuthorities;
import ru.lexender.icarusdb.auth.core.account.service.AccountService;

import java.time.LocalDate;
import java.util.Set;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api/v1/account")
@Tag(name = "Account", description = "Account API endpoints")
@Validated
public class AccountController {
    AccountService accountService;
    AccountLogService accountLogService;
    AccountMapper accountMapper;

    public Mono<ResponseEntity<AccountUserResponse>> createAccount(@Valid @RequestBody
                                                                   AccountCreationRequest request) {
        return accountService.count().flatMap(count -> {
                    Account account = accountMapper.accountCreationRequestToAccount(request);
                    if (count == 0) account.setAccountAuthorities(Set.of(AccountAuthorities.ROLE_STAFF));
                    return accountLogService.
                            save(AccountLog
                                    .builder()
                                    .email(request.email())
                                    .username(request.username())
                                    .build())
                            .then(accountService.save(account));
                }).map(accountMapper::accountToAccountUserResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PatchMapping("/{username}/lock")
    public Mono<ResponseEntity<Void>> lockAccountByUsername(@PathVariable String username,
                                                         @RequestBody AccountLockRequest request) {
        return accountService.lockByUsername(username, request.lockUntil())
                .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }

    @PatchMapping("/{username}/unlock")
    public Mono<ResponseEntity<?>> unlockAccountByUsername(@PathVariable String username) {
        return accountService.unlockByUsername(username)
                .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }

    @GetMapping("/me")
    public Mono<ResponseEntity<AccountAdminResponse>> getMe(@AuthenticationPrincipal Account account) {
        return Mono.just(account)
                .map(accountMapper::accountToAccountAdminResponse)
                .map(ResponseEntity::ok);
    }
}
