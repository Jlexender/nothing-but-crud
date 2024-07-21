package ru.lexender.icarusdb.struct.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.struct.core.dto.StructRequest;
import ru.lexender.icarusdb.struct.core.dto.StructResponse;
import ru.lexender.icarusdb.struct.core.model.Struct;
import ru.lexender.icarusdb.struct.core.service.StructService;

import java.util.UUID;

/**
 * This class is a controller that manages operations on Structs. It uses the
 * StructService for the business logic.
 *
 * @author Jlexender
 * @see StructService
 * @see Struct
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
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found all structs")})
    public ResponseEntity<Flux<StructResponse>> findAllStructs() {
        log.info("Fetching all structs");
        return ResponseEntity.ok(structService.findAll().map(struct -> {
            log.debug("Converting struct: {}", struct);
            return objectMapper.convertValue(struct, StructResponse.class);
        }));
    }

    /**
     * Deletes all Structs from the database.
     *
     * @return a Mono signaling completion
     */
    @DeleteMapping
    @Operation(summary = "Delete all structs", description = "Delete all structs from the database")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "All structs deleted")})
    public Mono<ResponseEntity<Void>> deleteAllStructs() {
        log.info("Deleting all structs");
        return structService.deleteAll().then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .doOnSuccess(response -> log.debug("All structs deleted successfully"))
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
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Struct created"),
        @ApiResponse(responseCode = "400", description = "Invalid struct supplied")})
    public Mono<ResponseEntity<UUID>> createStruct(
        @Valid @RequestBody @Parameter(description = "New struct") StructRequest struct) {
        log.info("Creating new struct: {}", struct);
        return structService.save(objectMapper.convertValue(struct, Struct.class))
            .map(Struct::getId)
                .map(ResponseEntity.status(HttpStatus.CREATED)::body)
                .doOnSuccess(response -> log.debug("Struct created with ID: {}", response.getBody()))
                .doOnError(e -> log.error("Error creating struct", e));
    }

    /**
     * Fetches a specific Struct from the database by its ID.
     *
     * @param id the ID of the Struct to fetch
     * @return a Mono of the fetched StructResponse
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a struct by ID", description = "Fetch a specific struct from the database by its ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the struct"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
        @ApiResponse(responseCode = "404", description = "Struct not found")})
    public Mono<ResponseEntity<StructResponse>> findStructById(
        @PathVariable @Parameter(description = "ID of the struct to be fetched") UUID id) {
        log.info("Fetching struct with ID: {}", id);
        return structService.findById(id).map(struct -> {
            StructResponse structResponse = objectMapper.convertValue(struct, StructResponse.class);
            return ResponseEntity.ok(structResponse);
        }).defaultIfEmpty(ResponseEntity.notFound().build()).doOnSuccess(response -> {
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.debug("Struct with ID {} not found", id);
            } else {
                log.debug("Struct found: {}", response.getBody());
            }
        }).doOnError(e -> log.error("Error fetching struct with ID: {}", id, e));
    }

    /**
     * Deletes a specific Struct from the database by its ID.
     *
     * @param id the ID of the Struct to delete
     * @return a Mono signaling completion
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a struct by ID", description = "Delete a specific struct from the database by its ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Struct deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
        @ApiResponse(responseCode = "404", description = "Struct not found")})
    public Mono<ResponseEntity<Void>> deleteStructById(
        @PathVariable @Parameter(description = "ID of the struct to be deleted") UUID id) {
        log.info("Deleting struct with ID: {}", id);
        return structService.existsById(id).flatMap(exists -> {
            if (exists) {
                return structService.deleteById(id)
                    .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                        .doOnSuccess(response -> log.debug("Struct with ID {} deleted successfully", id))
                        .doOnError(e -> log.error("Error deleting struct with ID: {}", id, e));
            } else {
                log.debug("Struct to delete with ID {} not found", id);
                return Mono.just(ResponseEntity.notFound().build());
            }
        });
    }

    /**
     * Counts all Structs in the database.
     *
     * @return a Mono of the count of all Structs
     */
    @GetMapping("/count")
    @Operation(summary = "Count all structs", description = "Count all structs in the database")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Counted all structs")})
    public Mono<ResponseEntity<Long>> countStructs() {
        log.info("Counting all structs");
        return structService.count().map(ResponseEntity.ok()::body)
                .doOnSuccess(response -> log.debug("Counted all structs: {}", response.getBody()))
                .doOnError(e -> log.error("Error counting all structs", e));
    }
}
