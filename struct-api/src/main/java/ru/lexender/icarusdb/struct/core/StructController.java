package ru.lexender.icarusdb.struct.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.struct.core.dto.StructResponse;
import ru.lexender.icarusdb.struct.core.model.Struct;
import ru.lexender.icarusdb.struct.core.service.StructService;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/struct")
public class StructController {
    StructService structService;
    ObjectMapper objectMapper;

    @GetMapping
    public Flux<Struct> findAll() {
        return structService.findAll();
    }

    @DeleteMapping
    public Mono<Void> deleteAll() {
        return structService.deleteAll();
    }

    @GetMapping("/{id}")
    public Mono<StructResponse> findById(@PathVariable UUID id) {
        return structService.findById(id)
                .map(struct -> objectMapper.convertValue(struct, StructResponse.class));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable UUID id) {
        return structService.deleteById(id);
    }

    @PostMapping
    public Mono<StructResponse> save(@RequestPart FilePart struct) {
        return structService.save(struct)
                .map(struct1 -> objectMapper.convertValue(struct1, StructResponse.class));
    }

    @PostMapping("/{id}")
    public Mono<Struct> updateById(@PathVariable UUID id, Struct struct) {
        return structService.updateById(id, struct);
    }
}
