package ru.lexender.icarusdb.auth.core;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.AccountController;
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
    AccountController accountController;
    RequestMapper requestMapper;


    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> signup(@Valid @RequestBody SignupRequest request) {
        return userDetailsService.findByUsername(request.username())
                .flatMap(userDetails -> {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            request.username(), request.password(), userDetails.getAuthorities()
                    );
                    return authenticationManager.authenticate(authToken)
                            .flatMap(authentication -> {
                                SecurityContext securityContext = new SecurityContextImpl(authentication);
                                return
                            });
                });
    }
}
