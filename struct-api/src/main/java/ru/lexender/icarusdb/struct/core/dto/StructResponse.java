package ru.lexender.icarusdb.struct.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record StructResponse(
        UUID id,
        @JsonProperty("structName") String struct_name,
        @JsonProperty("structData") byte[] struct_data
) {
}
