package ru.lexender.icarusdb.auth.exc;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleException(Exception ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error on serverside"))
                .doOnNext(responseEntity -> log.error("Unknown error on serverside", ex));
    }

    @ExceptionHandler(IOException.class)
    public Mono<ResponseEntity<String>> handleIOException(IOException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("IOException"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(cv -> cv.getPropertyPath().toString(), ConstraintViolation::getMessage));
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors));
    }

}
