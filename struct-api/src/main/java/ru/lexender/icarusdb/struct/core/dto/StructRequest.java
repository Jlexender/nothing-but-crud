package ru.lexender.icarusdb.struct.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

/**
 * This class represents the StructRequest data transfer object. It includes fields for
 * struct_name and struct_fields.
 *
 * @author Jlexender
 * @see NotBlank
 * @see NotNull
 */
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class StructRequest {

    @JsonProperty("struct_name")
    @Size(min = 1, max = 255)
    @NotBlank
    String structName;

    @JsonProperty("struct_fields")
    @NotNull
    Map<String, String> structFields;

}
