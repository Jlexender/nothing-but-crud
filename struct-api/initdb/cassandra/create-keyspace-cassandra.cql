CREATE KEYSPACE IF NOT EXISTS keyspace1
    WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };