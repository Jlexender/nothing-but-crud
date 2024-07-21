package ru.lexender.icarusdb.struct.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;


/**
 * This class represents the StructResponse data transfer object. It includes fields for
 * id, struct_name, struct_fields, and created_at.
 *
 * @author Jlexender
 */
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class StructResponse {

    @JsonProperty("id")
    UUID id;

    @JsonProperty("struct_name")
    String structName;

    @JsonProperty("struct_fields")
    Map<String, String> structFields;

    @JsonProperty("created_at")
    Timestamp createdAt;

}
