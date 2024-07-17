package ru.lexender.icarusdb.struct.core.model;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
@Table
public class Struct {
    @Builder.Default
    @PrimaryKey
    UUID id = UUID.randomUUID();

    @Column("struct_name")
    String structName;

    @Column("struct_data")
    ByteBuffer structData;

    @Builder.Default
    @Column("creation_date")
    Date creationDate = new Date();
}
