package ru.lexender.icarusdb.struct.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is a configuration class for Jackson. It includes a bean for ObjectMapper.
 *
 * @author Jlexender
 * @see ObjectMapper
 */
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Configuration
public class JacksonConfig {

    /**
     * Creates a bean for ObjectMapper. The bean is configured to use the SNAKE_CASE naming strategy
     *
     * @return the created ObjectMapper bean
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

}
