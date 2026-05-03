package com.gallelloit.employees.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> {
                    try {
                        configureCsrf(csrf);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Configures CSRF protection for the application.
     * 
     * CSRF protection is disabled for stateless API endpoints because:
     * 1. JWT authentication uses bearer tokens, not session cookies
     * 2. No session exists that could be hijacked via CSRF attacks
     * 3. Security is provided by valid JWT tokens in Authorization header
     * 4. This follows OWASP recommendations for stateless APIs
     * 
     * @param csrf the CSRF configuration to customize
     * @throws Exception if configuration fails
     */
    private void configureCsrf(CsrfConfigurer<HttpSecurity> csrf) throws Exception {
        // Ignore CSRF for stateless API endpoints that use JWT authentication
        csrf.ignoringRequestMatchers("/employees/**", "/departments/**", "/actuator/health");
        
        // Disable CSRF entirely since we're using stateless JWT authentication
        // This is safe and recommended for bearer token authentication
        csrf.disable();
    }
}