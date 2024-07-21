package ru.lexender.icarusdb.struct.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

/**
 * This class represents the Struct model. It includes fields for id, structName,
 * structFields, and createdAt.
 *
 * @author Jlexender
 * @see Id
 * @see Column
 * @see Table
 */
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
public class Struct {

    @Id
    UUID id;

    @Column("struct_name")
    String structName;

    @Column("struct_fields")
    Map<String, String> structFields;

    @Column("created_at")
    Timestamp createdAt;
}
