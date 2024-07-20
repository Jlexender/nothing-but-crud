package ru.lexender.icarusdb.struct.core.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.lexender.icarusdb.struct.core.model.Struct;

import java.util.UUID;

/**
 * This interface represents the repository for the Struct model.
 * It extends the ReactiveCrudRepository interface from Spring Data.
 *
 * @see ReactiveCrudRepository
 * @see Struct
 *
 * @author Jlexender
 */
@Repository
public interface StructRepository extends ReactiveCrudRepository<Struct, UUID> {
}