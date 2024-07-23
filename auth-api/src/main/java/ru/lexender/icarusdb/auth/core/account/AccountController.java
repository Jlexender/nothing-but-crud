package ru.lexender.icarusdb.auth.core.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.dto.AccountResponse;
import ru.lexender.icarusdb.auth.core.account.service.AccountService;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    ObjectMapper objectMapper;
    AccountService accountService;

    @PostMapping
    public Mono<Void> create() {
        return null; // todo
    }

    @GetMapping("/{username}")
    public Mono<AccountResponse> get(@PathVariable String username) {
        return accountService.findByUsername(username).map(
                account -> objectMapper.convertValue(account, AccountResponse.class)
        );
    }

    @PatchMapping("/{username}/lock")
    public Mono<Void> changeLock(@PathVariable String username,
                                 @RequestBody boolean lock) {
        return accountService.setLockByUsername(username, lock);
    }

    @PatchMapping("/{username}/password")
    public Mono<Void> changePassword(@PathVariable String username) {
        return null; // todo
    }

    @PatchMapping("/{username}/authorities")
    public Mono<Void> changeAuthorities(@PathVariable String username) {
        return null; // todo
    }



}
