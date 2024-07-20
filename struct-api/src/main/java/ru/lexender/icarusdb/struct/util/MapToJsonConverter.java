package ru.lexender.icarusdb.struct.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This class is a converter that converts a Map to a JSON string.
 * It uses Jackson's ObjectMapper for the conversion.
 *
 * @see ObjectMapper
 * @see Converter
 *
 * @author Jlexender
 */
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
@WritingConverter
public class MapToJsonConverter implements Converter<Map<String, String>, String> {
    ObjectMapper objectMapper;

    /**
     * Converts a Map to a JSON string.
     *
     * @param source the Map to convert
     * @return the converted JSON string
     * @throws RuntimeException if the conversion fails
     */
    @Override
    public String convert(@NonNull Map<String, String> source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert map to JSON string", e);
        }
    }
}