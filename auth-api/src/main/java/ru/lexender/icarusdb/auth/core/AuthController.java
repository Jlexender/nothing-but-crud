package ru.lexender.icarusdb.auth.core;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.AccountController;
import ru.lexender.icarusdb.auth.core.account.dto.AccountRequest;
import ru.lexender.icarusdb.auth.core.account.dto.AccountResponse;
import ru.lexender.icarusdb.auth.core.account.util.Password;
import ru.lexender.icarusdb.auth.core.dto.LoginRequest;
import ru.lexender.icarusdb.auth.core.dto.SignupRequest;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Authentication API endpoints")
@Validated
public class AuthController {
    AccountController accountController;
    ReactiveAuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Login to an account",
            description = "Login to an account with the provided credentials"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(ServerWebExchange exchange,
                                              @Valid @RequestBody LoginRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username().value(), request.password().value())
        ).map(authentication -> ResponseEntity.ok("Logged in as " + request.username().value()))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials")))
                .doOnSuccess(r -> log.info("Logged in as {}", request.username().value()))
                .doOnError(throwable -> log.error("Error on logging in: {}", throwable.getMessage()));
    }


    @Operation(
            summary = "Logout from an account",
            description = "Logout from an account"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged out"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(ServerWebExchange exchange) {
        return exchange.getSession()
                .flatMap(session -> session.invalidate()
                        .then(Mono.just(ResponseEntity.noContent().<Void>build())))
                .doOnSuccess(r -> log.info("Logged out"))
                .doOnError(throwable -> log.error("Error on logging out: {}", throwable.getMessage()));
    }

    @Operation(
            summary = "Signup for a new account",
            description = "Signup for a new account with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created account"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> signup(ServerWebExchange exchange,
                                               @Valid @RequestBody SignupRequest request) {
        return Mono.just(request)
                .flatMap(validRequest -> {
                    Password encoded = new Password(passwordEncoder.encode(validRequest.password().value()));
                    return accountController.create(exchange, new AccountRequest(validRequest.username(), encoded, validRequest.email()))
                                    .thenReturn(ResponseEntity.ok("Signed up as " + validRequest.username().value()))
                                    .doOnSuccess(response -> log.info("Successfully signed up as {}", validRequest.username().value()));
                    }
                )
                .onErrorResume(error -> {
                    log.error("Error during signup process: {}", error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred"));
                });
    }
}
