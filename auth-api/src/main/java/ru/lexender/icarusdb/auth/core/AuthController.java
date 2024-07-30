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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.AccountController;
import ru.lexender.icarusdb.auth.core.account.mapper.RequestMapper;
import ru.lexender.icarusdb.auth.core.dto.LoginRequest;
import ru.lexender.icarusdb.auth.core.dto.SignupRequest;
import ru.lexender.icarusdb.auth.kafka.ProducerService;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Authorization API endpoints")
@Validated
public class AuthController {
    AccountController accountController;
    ReactiveAuthenticationManager authenticationManager;
    RequestMapper requestMapper;
    ProducerService producerService;

    @Operation(
            summary = "Login",
            description = "Login with given credentials"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoginRequest.class))
    )
    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()))
                .map(auth -> {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    producerService.sendMessage("authorization-attempts",
                            "User " + auth.getName() + " has authorized");
                    return ResponseEntity.ok().body("Logged in as " + auth.getName());
                }).onErrorResume(e -> {
                    producerService.sendMessage("authorization-attempts",
                            "Failed authorization for user " + loginRequest.username());
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
                });
    }


    @Operation(
            summary = "Signup",
            description = "Signup with given credentials"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account created",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Signup request", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SignupRequest.class))
    )
    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return accountController.createAccount(requestMapper.signupRequestToAccountCreationRequest(signupRequest));
    }
}
