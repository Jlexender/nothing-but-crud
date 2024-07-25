package ru.lexender.icarusdb.auth.core.account.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import ru.lexender.icarusdb.auth.core.account.model.Account;

@Repository
public interface AccountRepository extends ReactiveCassandraRepository<Account, String> {
}
