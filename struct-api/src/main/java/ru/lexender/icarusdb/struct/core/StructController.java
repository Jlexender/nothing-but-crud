package ru.lexender.icarusdb.struct.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.struct.core.dto.StructRequest;
import ru.lexender.icarusdb.struct.core.dto.StructResponse;
import ru.lexender.icarusdb.struct.core.model.Struct;
import ru.lexender.icarusdb.struct.core.service.StructService;

import java.util.UUID;

/**
 * This class is a controller that manages operations on Structs.
 * It uses the StructService for the business logic.
 *
 * @see StructService
 * @see Struct
 *
 * @author Jlexender
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/struct")
@Tag(name = "Struct Controller", description = "Controller for managing structs")
@Log4j2
public class StructController {
    StructService structService;
    ObjectMapper objectMapper;

    /**
     * Fetches all Structs from the database.
     *
     * @return a Flux of all StructResponses
     */
    @GetMapping
    @Operation(summary = "Get all structs", description = "Fetch all structs from the database")
    public Flux<StructResponse> findAllStructs() {
        log.info("Fetching all structs");
        return structService.findAll()
                .map(struct -> {
                    log.debug("Mapping structs to StructResponses: {}", struct);
                    return objectMapper.convertValue(struct, StructResponse.class);
                });
    }

    /**
     * Deletes all Structs from the database.
     *
     * @return a Mono signaling completion
     */
    @DeleteMapping
    @Operation(summary = "Delete all structs", description = "Delete all structs from the database")
    public Mono<Void> deleteAllStructs() {
        log.info("Deleting all structs");
        return structService.deleteAll()
                .doOnSuccess(aVoid -> log.info("All structs deleted successfully"))
                .doOnError(e -> log.error("Error deleting all structs", e));
    }

    /**
     * Creates a new Struct in the database.
     *
     * @param struct the Struct to create
     * @return a Mono of the ID of the created Struct
     */
    @PostMapping
    @Operation(summary = "Create a struct", description = "Create a new struct in the database")
    public Mono<UUID> createStruct(@RequestBody @Parameter(description = "New struct") StructRequest struct) {
        log.info("Creating new struct: {}", struct);
        return Mono.just(objectMapper.convertValue(struct, Struct.class))
                .flatMap(structService::save)
                .map(Struct::getId)
                .doOnSuccess(id -> log.info("New struct created with ID: {}", id))
                .doOnError(e -> log.error("Error creating new struct", e));
    }

    /**
     * Fetches a specific Struct from the database by its ID.
     *
     * @param id the ID of the Struct to fetch
     * @return a Mono of the fetched StructResponse
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a struct by ID", description = "Fetch a specific struct from the database by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the struct"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Struct not found"),
    })
    public Mono<StructResponse> findStructById(@PathVariable @Parameter(description = "ID of the struct to be fetched") UUID id) {
        log.info("Fetching struct with ID: {}", id);
        return structService.findById(id)
                .map(struct -> {
                    log.debug("Mapping struct to StructResponse: {}", struct);
                    return objectMapper.convertValue(struct, StructResponse.class);
                })
                .doOnSuccess(structResponse -> log.info("Struct found: {}", structResponse))
                .doOnError(e -> log.error("Error fetching struct with ID: {}", id, e));
    }

    /**
     * Deletes a specific Struct from the database by its ID.
     *
     * @param id the ID of the Struct to delete
     * @return a Mono signaling completion
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a struct by ID", description = "Delete a specific struct from the database by its ID")
    public Mono<Void> deleteStructById(@PathVariable @Parameter(description = "ID of the struct to be deleted") UUID id) {
        log.info("Deleting struct with ID: {}", id);
        return structService.deleteById(id)
                .doOnSuccess(aVoid -> log.info("Struct with ID {} deleted successfully", id))
                .doOnError(e -> log.error("Error deleting struct with ID: {}", id, e));
    }
}