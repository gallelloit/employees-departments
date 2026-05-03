package com.gallelloit.employees.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

class CorsConfigUnitTest {

    @Test
    void shouldCreateCorsConfigurerBean() {
        CorsConfig corsConfig = new CorsConfig();

        WebMvcConfigurer configurer = corsConfig.corsConfigurer();

        assertThat(configurer).isNotNull();

        // Fuerza llamada real al método sobrescrito
        configurer.addCorsMappings(new org.springframework.web.servlet.config.annotation.CorsRegistry());

        assertThat(configurer).isInstanceOf(WebMvcConfigurer.class);
    }
}