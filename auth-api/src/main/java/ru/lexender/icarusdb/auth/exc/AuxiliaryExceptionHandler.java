package ru.lexender.icarusdb.auth.exc;

import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RestControllerAdvice
public class AuxiliaryExceptionHandler extends ResponseEntityExceptionHandler {
}
