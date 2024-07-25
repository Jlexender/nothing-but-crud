package ru.lexender.icarusdb.auth.core.account.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.service.AccountService;
import ru.lexender.icarusdb.auth.core.user.model.UserDetailsImpl;

import java.nio.ByteBuffer;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api/v1/image")
@Tag(name = "Image", description = "Image API endpoints")
@Validated
public class AccountImageController {
    AccountService accountService;

    @Operation(
            summary = "Upload an avatar",
            description = "Upload an avatar image and set it as the user's avatar"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Avatar uploaded"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/avatar")
    public Mono<ResponseEntity<Void>> uploadAvatar(@RequestPart(name = "avatar") Mono<ByteBuffer> avatar,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return avatar.flatMap(byteBuffer -> {
                    userDetails.getAccount().setAvatar(byteBuffer);
                    return accountService.save(userDetails.getAccount());
                })
                .doOnNext(account -> log.info("Avatar uploaded and persisted for user {}", userDetails.getUsername()))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
