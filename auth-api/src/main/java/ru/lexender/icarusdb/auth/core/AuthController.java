package ru.lexender.icarusdb.auth.core;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.AccountController;
import ru.lexender.icarusdb.auth.core.account.dto.AccountCreationRequest;
import ru.lexender.icarusdb.auth.core.account.mapper.AccountMapper;
import ru.lexender.icarusdb.auth.core.account.mapper.RequestMapper;
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
    ReactiveUserDetailsService userDetailsService;
    ReactiveAuthenticationManager authenticationManager;
    ServerSecurityContextRepository securityContextRepository;
    RequestMapper requestMapper;
    AccountController accountController;

    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> signup(@Valid @RequestBody SignupRequest request,
                                               ServerWebExchange exchange) {

        return accountController.createAccount(requestMapper
                        .signupRequestToAccountCreationRequest(request))
                .then(userDetailsService.findByUsername(request.username()))
                .flatMap(userDetails -> {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            request.username(), request.password(), userDetails.getAuthorities());

                    return authenticationManager.authenticate(authToken)
                            .flatMap(authentication -> {
                                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                                securityContext.setAuthentication(authentication);

                                return securityContextRepository.save(exchange, securityContext)
                                        .then(Mono.just(ResponseEntity.ok("Signed up as " + request.username())));
                            })
                            .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body("Authentication failed: " + ex.getMessage())));
                })
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Signup failed: " + ex.getMessage())));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@Valid @RequestBody LoginRequest request, ServerWebExchange exchange) {
        return userDetailsService.findByUsername(request.username())
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")))
                .flatMap(userDetails -> {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            request.username(), request.password(), userDetails.getAuthorities());

                    return authenticationManager.authenticate(authToken)
                            .flatMap(authentication -> {
                                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                                securityContext.setAuthentication(authentication);

                                return securityContextRepository.save(exchange, securityContext)
                                        .then(Mono.just(ResponseEntity.ok("Logged in as " + request.username())));
                            })
                            .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body("Authentication failed")));
                })
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authentication failed")));
    }


    @PostMapping("/logout")
    public Mono<ResponseEntity<String>> logout(ServerWebExchange exchange) {
        return exchange.getSession().flatMap(
                session -> session.invalidate().thenReturn(ResponseEntity.ok("Logged out"))
        );
    }
}
