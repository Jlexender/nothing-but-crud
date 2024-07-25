package ru.lexender.icarusdb.auth.core.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.dto.AccountCreationRequest;
import ru.lexender.icarusdb.auth.core.account.dto.AccountLockRequest;
import ru.lexender.icarusdb.auth.core.account.log.model.AccountLog;
import ru.lexender.icarusdb.auth.core.account.log.service.AccountLogService;
import ru.lexender.icarusdb.auth.core.account.mapper.AccountMapper;
import ru.lexender.icarusdb.auth.core.account.model.Account;
import ru.lexender.icarusdb.auth.core.account.model.AccountRole;
import ru.lexender.icarusdb.auth.core.account.service.AccountService;
import ru.lexender.icarusdb.auth.core.user.model.UserDetailsImpl;

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
    PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Create account",
            description = "Create account with given credentials"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account created",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account creation request", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountCreationRequest.class))
    )
    @PostMapping
    public Mono<ResponseEntity<String>> createAccount(@Valid @RequestBody AccountCreationRequest request) {
        return Mono.zip(
                accountLogService.existsByEmail(request.email()),
                accountService.existsByUsername(request.username()),
                accountService.count()
        ).flatMap(tuple -> {
            boolean emailExists = tuple.getT1();
            boolean usernameExists = tuple.getT2();
            long count = tuple.getT3();

            if (usernameExists)
                return Mono.error(new RuntimeException("Username is already taken"));
            if (emailExists)
                return Mono.error(new RuntimeException("Email is already taken"));


            Account account = accountMapper.accountCreationRequestToAccount(request);
            if (count == 0) {
                account.setRole(AccountRole.ROLE_STAFF);
            }
            account.setPassword(passwordEncoder.encode(request.password()));

            return accountLogService.save(AccountLog.builder()
                            .username(request.username())
                            .email(request.email())
                            .build()).then(accountService.save(account));
        }).map(account -> ResponseEntity.ok(
                "Created account %s:%s:%s".formatted(
                        account.getEmail(),
                        account.getUsername(),
                        account.getRole())
                )
        );
    }

    @Operation(
            summary = "Get account",
            description = "Get account by username"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "404", description = "Account not found")

    })
    @GetMapping("/{username}")
    public Mono<ResponseEntity<Record>> getAccount(@Parameter(description = "Username") @PathVariable String username,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return accountService.findByUsername(username)
                .map(account -> {
                    if (userDetails != null
                            && userDetails.getAccount().getRole().compareTo(AccountRole.ROLE_ADMIN) >= 0) {
                        return accountMapper.accountToAccountAdminResponse(account);
                    } else {
                        return accountMapper.accountToAccountUserResponse(account);
                    }
                })
                .map(ResponseEntity::ok).switchIfEmpty(
                        Mono.fromCallable(() -> ResponseEntity.notFound().build())
                );
    }

    @Operation(
            summary = "Lock account",
            description = "Lock account by username"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account locked"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account lock request", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountLockRequest.class))
    )
    @PatchMapping("/{username}/lock")
    public Mono<ResponseEntity<Void>> lockAccount(@Parameter(description = "Username")
                                                  @PathVariable String username,
                                                  @Valid @RequestBody AccountLockRequest request) {
        return accountService.lockByUsername(username, request.lockUntil())
                .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }

    @Operation(
            summary = "Unlock account",
            description = "Unlock account by username"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account unlocked"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PatchMapping("/{username}/unlock")
    public Mono<ResponseEntity<Void>> unlockAccount(@Parameter(description = "Username")
                                                    @PathVariable String username) {
        return accountService.unlockByUsername(username)
                .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }

    @Operation(
            summary = "Update account authority",
            description = "Update account authority by username"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account authority updated"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account role", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountRole.class))
    )
    @PatchMapping("/{username}/authority")
    public Mono<ResponseEntity<Void>> updateAuthority(@Parameter(description = "Username")
                                                      @PathVariable String username,
                                                      @Valid @RequestBody AccountRole authority,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return accountService.findByUsername(username)
                .filter(account -> account.getRole().compareTo(userDetails.getAccount().getRole()) < 0)
                .flatMap(account -> accountService.updateAuthoritiesByUsername(username, authority))
                .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }
}
