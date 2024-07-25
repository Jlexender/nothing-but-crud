package ru.lexender.icarusdb.auth.core.account;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.lexender.icarusdb.auth.core.account.mapper.AccountMapper;
import ru.lexender.icarusdb.auth.core.account.service.AccountService;

@WebFluxTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountService accountService;
    @MockBean
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountController accountController;

}
