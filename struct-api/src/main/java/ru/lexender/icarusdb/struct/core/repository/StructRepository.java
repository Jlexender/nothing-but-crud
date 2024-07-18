package ru.lexender.icarusdb.struct.core.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.lexender.icarusdb.struct.core.model.Struct;

import java.util.UUID;

@Repository
public interface StructRepository extends ReactiveCrudRepository<Struct, UUID> {
}
