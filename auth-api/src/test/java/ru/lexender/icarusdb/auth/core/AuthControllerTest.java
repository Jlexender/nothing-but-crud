package ru.lexender.icarusdb.auth.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import ru.lexender.icarusdb.auth.core.account.service.AccountService;

@WebFluxTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountService accountService;

    @InjectMocks
    private AuthController authController;

}
