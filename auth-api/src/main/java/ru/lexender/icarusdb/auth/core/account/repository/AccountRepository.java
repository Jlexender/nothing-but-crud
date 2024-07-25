package ru.lexender.icarusdb.auth.core.account.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.auth.core.account.model.Account;

import java.util.UUID;

@Repository
public interface AccountRepository extends ReactiveCassandraRepository<Account, String> {
}
