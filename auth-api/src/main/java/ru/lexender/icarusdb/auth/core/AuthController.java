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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
import ru.lexender.icarusdb.auth.core.user.model.UserDetailsImpl;

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
    ServerSecurityContextRepository securityContextRepository;
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
                        new UsernamePasswordAuthenticationToken(request.username().value(), request.password().value()))
                .flatMap(authentication -> exchange.getSession()
                        .doOnNext(session -> {
                            session.getAttributes().put("username", request.username().value());
                        })
                        .then(Mono.just(ResponseEntity.ok("Logged in as " + request.username().value())))
                        .doOnSuccess(response -> log.info("Logged in as {}", request.username().value())))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials")))
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
        return accountController.getByUsername(exchange, request.username())
                .flatMap(existingAccount -> Mono.just(ResponseEntity.badRequest().body("Username already exists")))
                .switchIfEmpty(
                        Mono.defer(() -> {
                            Password hashed = new Password(passwordEncoder.encode(request.password().value()));
                            AccountRequest accountRequest =
                                    new AccountRequest(request.username(), hashed, request.email());

                            return accountController.create(exchange, accountRequest)
                                    .then(Mono.defer(() -> {
                                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                                request.username().value(), request.password().value());

                                        return authenticationManager.authenticate(authentication)
                                                .flatMap(auth -> {
                                                    SecurityContext securityContext = new SecurityContextImpl(auth);
                                                    return securityContextRepository.save(exchange, securityContext)
                                                            .thenReturn(ResponseEntity.ok("Signed up as " + request.username().value()));
                                                });
                                    }));
                        })
                )
                .doOnSuccess(r -> log.info("Signed up as {}", request.username().value()))
                .doOnError(throwable -> log.error("Error on signing up: {}", throwable.getMessage()));
    }


    @Operation(
            summary = "Get current user",
            description = "Get current user for the session"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved current user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailsImpl.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/profile")
    public Mono<ResponseEntity<UserDetailsImpl>> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.debug("Current user: {}", userDetails);
        return Mono.just(ResponseEntity.ok(userDetails));
    }
}
