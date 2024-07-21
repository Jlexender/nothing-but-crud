package ru.lexender.icarusdb.struct.config;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import ru.lexender.icarusdb.struct.util.JsonToMapConverter;
import ru.lexender.icarusdb.struct.util.MapToJsonConverter;

import java.util.List;

/**
 * This class is a configuration class for R2DBC. It includes a bean for
 * R2dbcCustomConversions.
 *
 * @author Jlexender
 * @see R2dbcCustomConversions
 */
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Configuration
public class R2dbcConfig {

    JsonToMapConverter jsonToMapConverter;

    MapToJsonConverter mapToJsonConverter;

    @Bean
    public R2dbcCustomConversions customConversions() {
        return R2dbcCustomConversions.of(MySqlDialect.INSTANCE,
                List.of(jsonToMapConverter, mapToJsonConverter));
    }

}
