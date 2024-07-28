package ru.lexender.icarusdb.auth.core.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.dto.AccountAdminResponse;
import ru.lexender.icarusdb.auth.core.account.dto.AccountUserResponse;
import ru.lexender.icarusdb.auth.core.account.log.model.AccountLog;
import ru.lexender.icarusdb.auth.core.account.log.service.AccountLogService;
import ru.lexender.icarusdb.auth.core.account.mapper.AccountMapper;
import ru.lexender.icarusdb.auth.core.account.model.Account;
import ru.lexender.icarusdb.auth.core.account.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.lexender.icarusdb.auth.core.config.TestSecurityConfig;
import ru.lexender.icarusdb.auth.core.user.model.UserDetailsImpl;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@WebFluxTest(AccountController.class)
@Import(TestSecurityConfig.class)
public class AccountControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountLogService accountLogService;

    @MockBean
    private AccountMapper accountMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        when(accountLogService.existsByEmail(any())).thenReturn(Mono.just(false));
        when(accountService.existsByUsername(any())).thenReturn(Mono.just(false));
        when(accountService.count()).thenReturn(Mono.just(0L));
        when(accountLogService.save(any())).thenReturn(Mono.just(new AccountLog()));
        when(accountService.save(any())).thenReturn(Mono.just(new Account()));
        when(passwordEncoder.encode(any())).thenReturn(UUID.randomUUID().toString());
        when(accountMapper.accountCreationRequestToAccount(any())).thenReturn(new Account());
        when(accountMapper.accountToAccountAdminResponse(any())).thenReturn(AccountAdminResponse
                .builder().username("test").build()
        );
        when(accountMapper.accountToAccountUserResponse(any())).thenReturn(AccountUserResponse
                .builder().username("test").build()
        );
    }

    @Test
    void createAccount_shouldCreateAccount() {
        String jsonRequest = """
            {
              "username": "test",
              "password": "Localhost1",
              "name": "test",
              "email": "test@localhost"
            }
            """;


        webTestClient.post()
                .uri("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void createAccount_shouldReturnBadRequest() {
        String jsonRequest = """
            {
              "username": "test",
              "password": "Localhost1",
              "name": "test",
              "email": "test@localhost"
            }
            """;

        when(accountService.existsByUsername(any())).thenReturn(Mono.just(true));

        webTestClient.post()
                .uri("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createAccount_shouldReturnBadRequest2() {
        String jsonRequest = """
            {
              "username": "test",
              "password": "Localhost1",
              "name": "test",
              "email": "test@localhost"
            }
            """;

        when(accountLogService.existsByEmail(any())).thenReturn(Mono.just(true));

        webTestClient.post()
                .uri("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getAccount_shouldReturnAccount() {
        UserDetailsImpl mockUserDetails = Mockito.mock(UserDetailsImpl.class);
        when(mockUserDetails.getUsername()).thenReturn("testuser");
        doReturn(new Account()).when(mockUserDetails).getAccount();
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .when(mockUserDetails).getAuthorities();

        when(accountService.findByUsername(any())).thenReturn(Mono.just(new Account()));

        webTestClient.mutateWith(SecurityMockServerConfigurers.mockUser(mockUserDetails))
                .get()
                .uri("/api/v1/account/test")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getAccount_shouldReturnNotFound() {
        when(accountService.findByUsername(any())).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/account/test")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAccount_shouldReturnBadRequest() {
        webTestClient.get()
                .uri("/api/v1/account")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
