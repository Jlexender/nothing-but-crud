package ru.lexender.icarusdb.auth.core.account.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.model.Account;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends ReactiveCassandraRepository<Account, UUID>,
                                            PagingAndSortingRepository<Account, UUID> {
    Mono<Account> findByUsername(String username);
}
