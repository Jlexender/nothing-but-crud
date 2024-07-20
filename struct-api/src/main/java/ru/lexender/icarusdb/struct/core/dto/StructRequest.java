package ru.lexender.icarusdb.struct.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

/**
 * This class represents the StructRequest data transfer object.
 * It includes fields for struct_name and struct_fields.
 *
 * @see NotBlank
 * @see NotNull
 *
 * @author Jlexender
 */
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class StructRequest {
    @Size(min = 1, max = 255)
    @NotBlank
    String struct_name;

    @NotNull
    Map<String, String> struct_fields;
}