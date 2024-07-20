package ru.lexender.icarusdb.struct.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

/**
 * This class represents the StructResponse data transfer object.
 * It includes fields for id, struct_name, struct_fields, and created_at.
 *
 * @author Jlexender
 */
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class StructResponse {
    UUID id;

    String struct_name;

    Map<String, String> struct_fields;

    Timestamp created_at;
}