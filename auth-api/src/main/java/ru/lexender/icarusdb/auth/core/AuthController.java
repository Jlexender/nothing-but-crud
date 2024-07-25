package ru.lexender.icarusdb.auth.core;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.AccountController;
import ru.lexender.icarusdb.auth.core.account.dto.AccountCreationRequest;
import ru.lexender.icarusdb.auth.core.account.log.model.AccountLog;
import ru.lexender.icarusdb.auth.core.account.log.service.AccountLogService;
import ru.lexender.icarusdb.auth.core.account.mapper.AccountMapper;
import ru.lexender.icarusdb.auth.core.account.mapper.RequestMapper;
import ru.lexender.icarusdb.auth.core.account.service.AccountService;
import ru.lexender.icarusdb.auth.core.dto.LoginRequest;
import ru.lexender.icarusdb.auth.core.dto.PasswordUpdateRequest;
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
    AccountLogService accountLogService;
    AccountController accountController;

    @PostMapping("/signup")
    public Mono<ResponseEntity<Void>> signup(@Valid @RequestBody SignupRequest request) {
        return
    }
}
