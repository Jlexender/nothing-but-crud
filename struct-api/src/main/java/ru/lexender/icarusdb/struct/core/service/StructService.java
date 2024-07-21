package ru.lexender.icarusdb.struct.core.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.struct.core.model.Struct;
import ru.lexender.icarusdb.struct.core.repository.StructRepository;

import java.util.UUID;

/**
 * This class provides services for the Struct model. It uses the StructRepository to
 * interact with the database.
 *
 * @author Jlexender
 * @see StructRepository
 * @see Struct
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
        log.debug("Fetching all structs");
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
    @CacheEvict(value = "structs", allEntries = true)
    public Mono<Void> deleteAll() {
        log.debug("Deleting all structs");
        return structRepository.deleteAll()
                .doOnSuccess(aVoid -> log.debug("All structs deleted successfully"))
                .doOnError(e -> log.error("Error deleting all structs", e));
    }

    /**
     * Fetches a Struct by its ID.
     *
     * @param id the ID of the Struct to fetch
     * @return a Mono of the fetched Struct
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "struct", key = "#id")
    public Mono<Struct> findById(UUID id) {
        log.info("Fetching struct with ID: {}", id);
        return structRepository.findById(id)
                .doOnError(e -> log.error("Error fetching struct with ID: {}", id, e));
    }

    /**
     * Deletes a Struct by its ID.
     *
     * @param id the ID of the Struct to delete
     * @return a Mono signaling completion
     */
    @Transactional
    @CacheEvict(value = "struct", key = "#id")
    public Mono<Void> deleteById(UUID id) {
        log.debug("Deleting struct with ID: {}", id);
        return structRepository.deleteById(id)
                .doOnSuccess(aVoid -> log.debug("Struct with ID {} deleted successfully", id))
                .doOnError(e -> log.error("Error deleting struct with ID: {}", id, e));
    }

    /**
     * Saves a Struct to the database.
     *
     * @param struct the Struct to save
     * @return a Mono of the saved Struct
     */
    @Transactional
    @CachePut(value = "struct", key = "#struct.id")
    public Mono<Struct> save(Struct struct) {
        log.debug("Saving struct: {}", struct);
        return structRepository.save(struct)
                .doOnSuccess(savedStruct -> log.debug("Struct saved successfully: {}", savedStruct))
                .doOnError(e -> log.error("Error saving struct", e));
    }

    /**
     * Checks if a Struct with the given ID exists in the database.
     *
     * @param id the ID of the Struct to update
     * @return a Mono of the existence of the Struct
     */
    @Transactional(readOnly = true)
    public Mono<Boolean> existsById(UUID id) {
        log.debug("Checking if struct with ID {} exists", id);
        return structRepository.existsById(id)
                .doOnSuccess(exists -> log.debug("Struct with ID {} exists: {}", id, exists))
                .doOnError(e -> log.error("Error checking if struct with ID {} exists", id, e));
    }

    /**
     * Counts all Structs in the database.
     *
     * @return a Mono of the count of all Structs
     */
    @Transactional(readOnly = true)
    public Mono<Long> count() {
        log.debug("Counting all structs");
        return structRepository.count()
                .doOnSuccess(count -> log.debug("Counted all structs: {}", count))
                .doOnError(e -> log.error("Error counting all structs", e));
    }

}
