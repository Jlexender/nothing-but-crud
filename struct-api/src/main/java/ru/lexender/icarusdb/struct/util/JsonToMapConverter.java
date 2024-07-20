package ru.lexender.icarusdb.struct.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * This class is a converter that converts a JSON string to a Map.
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
public class JsonToMapConverter implements Converter<String, Map<String, String>> {

    private final ObjectMapper objectMapper;

    /**
     * Converts a JSON string to a Map.
     *
     * @param source the JSON string to convert
     * @return the converted Map
     * @throws RuntimeException if the conversion fails
     */
    @Override
    public Map<String, String> convert(@NonNull String source) {
        try {
            return objectMapper.readValue(source, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Could not convert JSON string to map", e);
        }
    }
}