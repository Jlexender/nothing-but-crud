package ru.lexender.icarusdb.struct.core.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
@Table
public class Struct {
    @Builder.Default
    @Id
    UUID id = UUID.randomUUID();

    @Column("struct_name")
    String structName;

    @Column("struct_data")
    ByteBuffer structData;

    @Builder.Default
    @Column("creation_date")
    Date creationDate = new Date();
}