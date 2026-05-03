package com.gallelloit.employees.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

class CorsConfigUnitTest {

    @Test
    void shouldExecuteCorsConfiguration() {
        CorsConfig corsConfig = new CorsConfig();

        WebMvcConfigurer configurer = corsConfig.corsConfigurer();

        assertThat(configurer).isNotNull();

        CorsRegistry registry = new CorsRegistry();

        configurer.addCorsMappings(registry);

        assertThat(registry).isNotNull();
    }
}