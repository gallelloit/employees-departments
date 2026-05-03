package com.gallelloit.employees.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

class CorsConfigUnitTest {

    @Test
    void shouldExecuteCorsConfiguration() {
        CorsConfig corsConfig = new CorsConfig();

        WebMvcConfigurer configurer = corsConfig.corsConfigurer();

        CorsRegistry registry = new CorsRegistry();

        configurer.addCorsMappings(registry);
    }
}