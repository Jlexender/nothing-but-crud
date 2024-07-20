package ru.lexender.icarusdb.struct.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * This class is a configuration class for WebFlux.
 *
 * @see WebFluxConfigurer
 *
 * @author Jlexender
 */
@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

}