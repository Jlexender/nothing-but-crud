package ru.lexender.icarusdb.auth.core.account.log.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import ru.lexender.icarusdb.auth.core.account.log.model.AccountLog;

@Repository
public interface AccountLogRepository extends ReactiveCassandraRepository<AccountLog, String> {
}
