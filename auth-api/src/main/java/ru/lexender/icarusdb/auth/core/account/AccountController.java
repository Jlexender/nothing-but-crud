package ru.lexender.icarusdb.auth.core.account;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.dto.AccountRequest;
import ru.lexender.icarusdb.auth.core.account.dto.AccountResponse;
import ru.lexender.icarusdb.auth.core.account.model.Account;
import ru.lexender.icarusdb.auth.core.account.model.AccountAuthorities;
import ru.lexender.icarusdb.auth.core.account.service.AccountService;
import ru.lexender.icarusdb.auth.core.account.util.Password;
import ru.lexender.icarusdb.auth.core.account.util.Username;

import java.util.Set;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
@Validated
@RestController
@RequestMapping("/api/v1/auth/account")
@Tag(name = "Account", description = "Account API endpoints")
public class AccountController {
    ObjectMapper objectMapper;
    AccountService accountService;


    @Operation(
            summary = "Create a new account",
            description = "Create a new account with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created account",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public Mono<ResponseEntity<AccountResponse>> create(@Valid @RequestBody AccountRequest request) {
        return accountService.save(objectMapper.convertValue(request, Account.class))
                .map(account -> objectMapper.convertValue(account, AccountResponse.class))
                .map(ResponseEntity::ok)
                .doOnSuccess(accountResponseResponseEntity -> log.info("Created successfully: {}", request.username()))
                .doOnError(throwable -> log.error("Error on creating account: {}", throwable.getMessage()));
    }

    @Operation(
            summary = "Retrieve an account by username",
            description = "Retrieve account details using username"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{username}")
    public Mono<ResponseEntity<AccountResponse>> getByUsername(@Parameter(description = "Username of the account")
                                                              @PathVariable Username username) {
        return accountService.findByUsername(username.value())
                .map(account -> objectMapper.convertValue(account, AccountResponse.class))
                .map(ResponseEntity::ok)
                .doOnSuccess(accountResponseResponseEntity -> log.info("Account retrieved: {}", username))
                .doOnError(throwable -> log.error("Error retrieving account: {}", throwable.getMessage()));
    }

    @Operation(
            summary = "Change lock status for an account",
            description = "Lock or unlock an account by username"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully changed lock status"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/{username}/lock")
    public Mono<ResponseEntity<Void>> changeLockByUsername(@Parameter(description = "Username of the account")
                                                              @PathVariable Username username,
                                                              @Parameter(description = "New lock status")
                                                              @RequestParam boolean lock) {
        return accountService.setLockByUsername(username.value(), lock)
                .thenReturn(ResponseEntity.noContent().<Void>build())
                .doOnSuccess(aVoid -> log.info("Account locked: {}", username))
                .doOnError(throwable -> log.error("Error locking account: {}", throwable.getMessage()));
    }

    @Operation(
            summary = "Change password for an account",
            description = "Change the password of an account by username"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully changed password"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/password")
    public Mono<ResponseEntity<Void>> changePasswordByUsername(@Parameter(description = "Username of the account")
                                                                   @Valid @RequestBody Password newPassword,
                                                               @AuthenticationPrincipal Username username) {
        log.debug("Changing password for: {}", username);
        return accountService.findByUsername(username.value())
                .flatMap(account -> {
                    account.setPassword(newPassword.value());
                    return accountService.save(account);
                }).thenReturn(ResponseEntity.noContent().<Void>build())
                .doOnSuccess(aVoid -> log.info("Password changed: {}", username))
                .doOnError(throwable -> log.error("Error changing password: {}", throwable.getMessage()));
    }

    @Operation(
            summary = "Change authorities for an account",
            description = "Change the authorities of an account by username"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully changed authorities"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/{username}/authorities")
    public Mono<ResponseEntity<Void>> changeAuthoritiesByUsername(@Parameter(description = "Username of the account")
                                                                    @PathVariable Username username,
                                                                    @RequestBody Set<AccountAuthorities> authorities) {
        log.debug("Changing authorities for: {}", username);
        return accountService.findByUsername(username.value())
                .flatMap(account -> {
                    account.setAccountAuthorities(authorities);
                    return accountService.save(account);
                }).thenReturn(ResponseEntity.noContent().<Void>build())
                .doOnSuccess(aVoid -> log.info("Authorities changed: {}", username))
                .doOnError(throwable -> log.error("Error changing authorities: {}", throwable.getMessage()));
    }

    @GetMapping("/test") // TODO: Remove this endpoint
    public Mono<ResponseEntity<String>> helloTest(ServerWebExchange exchange) {
        // Get userdetails from the session
        return exchange.getSession().map(session -> {
            String username = session.getAttribute("username");
            return ResponseEntity.ok("Hello, " + username);
        });
    }
}
