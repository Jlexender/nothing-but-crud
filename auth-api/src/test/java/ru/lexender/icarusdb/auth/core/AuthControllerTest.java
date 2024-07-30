package ru.lexender.icarusdb.auth.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.AccountController;
import ru.lexender.icarusdb.auth.core.config.TestSecurityConfig;
import ru.lexender.icarusdb.auth.core.dto.LoginRequest;
import ru.lexender.icarusdb.auth.core.dto.SignupRequest;
import ru.lexender.icarusdb.auth.core.account.mapper.RequestMapper;
import ru.lexender.icarusdb.auth.kafka.ProducerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(AuthController.class)
@Import(TestSecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReactiveAuthenticationManager authenticationManager;

    @MockBean
    private AccountController accountController;

    @MockBean
    private ProducerService producerService;

    @MockBean
    private RequestMapper requestMapper;

    @BeforeEach
    void setUp() {
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        when(authenticationManager.authenticate(any())).thenReturn(Mono.just(auth));
        when(accountController.createAccount(any())).thenReturn(Mono.just(ResponseEntity.ok("Account created")));

    }

    @Test
    void login_shouldReturnToken() {
        LoginRequest loginRequest = new LoginRequest("testuser", "password");

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody != null;
                    assert responseBody.contains("Logged in as testuser");
                });
    }

    @Test
    void signup_shouldCreateAccount() {
        when(accountController.createAccount(any())).thenReturn(
                Mono.just(ResponseEntity.ok("Created account test:name"))
        );

        SignupRequest signupRequest = SignupRequest.builder()
                .username("test")
                .name("name")
                .password("Localhost1")
                .passwordConfirm("Localhost1")
                .email("test@localhost")
                .build();

        webTestClient.post()
                .uri("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signupRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody != null;
                    assert responseBody.equals("Created account test:name");
                });
    }
}
