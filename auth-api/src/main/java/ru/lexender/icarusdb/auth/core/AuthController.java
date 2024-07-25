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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.AccountController;
import ru.lexender.icarusdb.auth.core.account.dto.AccountResponse;
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
        log.debug("Login request: {}", request);
        return exchange.getSession().map(session -> {
            session.getAttributes().put("username", request.username().value());
            return ResponseEntity.ok("Logged in as " + request.username().value());
        });
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
        // TODO: Implement account creation
        return null;
    }
}
