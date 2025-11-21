package org.util.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.HttpSecurityDsl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Enable CORS using the CorsConfigurationSource bean below
                .cors(Customizer.withDefaults())

                // Disable CSRF if you're using a stateless API / JWT, etc.
                .csrf(csrf -> csrf.disable())

                // Session management / stateless (optional, but typical for APIs)
                // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configure which endpoints are public vs protected
                .authorizeHttpRequests(auth -> auth
                        // Preflight requests should always be allowed
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // Public endpoints (adjust to your app)
                        .requestMatchers(
                                "/auth/**",
                                "/api/**",
                                "/actuator/health"
                        ).permitAll()

                        // Everything else requires auth
                        .anyRequest().authenticated()
                );

        // Add other security config here (JWT filters, OAuth2, etc.)

        return http.build();
    }
}
