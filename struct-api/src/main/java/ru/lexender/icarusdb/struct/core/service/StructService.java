package ru.lexender.icarusdb.struct.core.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.struct.core.model.Struct;
import ru.lexender.icarusdb.struct.core.repository.StructRepository;

import java.util.Objects;
import java.util.UUID;

/**
 * This class provides services for the Struct model.
 * It uses the StructRepository to interact with the database.
 *
 * @see StructRepository
 * @see Struct
 *
 * @author Jlexender
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
@Log4j2
public class StructService {
    StructRepository structRepository;

    /**
     * Fetches all Structs from the database.
     *
     * @return a Flux of all Structs
     */
    @Transactional(readOnly = true)
    public Flux<Struct> findAll() {
        log.info("Fetching all structs");
        return structRepository.findAll()
                .doOnNext(struct -> log.debug("Fetched struct: {}", struct))
                .doOnError(e -> log.error("Error fetching all structs", e));
    }

    /**
     * Deletes all Structs from the database.
     *
     * @return a Mono signaling completion
     */
    @Transactional
    public Mono<Void> deleteAll() {
        log.info("Deleting all structs");
        return structRepository.deleteAll()
                .doOnSuccess(aVoid -> log.info("All structs deleted successfully"))
                .doOnError(e -> log.error("Error deleting all structs", e));
    }

    /**
     * Fetches a Struct by its ID.
     *
     * @param id the ID of the Struct to fetch
     * @return a Mono of the fetched Struct
     */
    @Transactional(readOnly = true)
    public Mono<Struct> findById(UUID id) {
        log.info("Fetching struct with ID: {}", id);
        return structRepository.findById(id)
                .doOnSuccess(struct -> {
                    if (struct != null) {
                        log.info("Fetched struct with ID {}: {}", id, struct);
                    } else {
                        log.warn("No struct found with ID: {}", id);
                    }
                })
                .doOnError(e -> log.error("Error fetching struct with ID: {}", id, e));
    }

    /**
     * Deletes a Struct by its ID.
     *
     * @param id the ID of the Struct to delete
     * @return a Mono signaling completion
     */
    @Transactional
    public Mono<Void> deleteById(UUID id) {
        log.info("Deleting struct with ID: {}", id);
        return structRepository.deleteById(id)
                .doOnSuccess(aVoid -> log.info("Struct with ID {} deleted successfully", id))
                .doOnError(e -> log.error("Error deleting struct with ID: {}", id, e));
    }

    /**
     * Saves a Struct to the database.
     *
     * @param struct the Struct to save
     * @return a Mono of the saved Struct
     */
    @Transactional
    public Mono<Struct> save(Struct struct) {
        log.info("Saving struct: {}", struct);
        return Mono.just(struct)
                .flatMap(structRepository::save)
                .doOnSuccess(savedStruct -> log.info("Struct saved successfully: {}", savedStruct))
                .doOnError(e -> log.error("Error saving struct", e));
    }
}