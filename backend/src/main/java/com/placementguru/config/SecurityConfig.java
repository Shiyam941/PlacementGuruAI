package com.placementguru.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // 🔐 Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔐 Security Filter Chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ❌ Disable CSRF (JWT project)
            .csrf(csrf -> csrf.disable())

            // ✅ CORS Configuration
            .cors(cors -> cors.configurationSource(request -> {

                CorsConfiguration corsConfig = new CorsConfiguration();

                // Allow frontend origin
                corsConfig.setAllowedOriginPatterns(
                        List.of("http://localhost:3000", "http://localhost:3001")
                );

                corsConfig.setAllowedMethods(
                        List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
                );

                corsConfig.setAllowedHeaders(List.of("*"));

                corsConfig.setAllowCredentials(true);

                return corsConfig;
            }))

            // ❌ Stateless (No Session)
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔐 Authorization Rules
            .authorizeHttpRequests(auth -> auth
                    // 🔥 VERY IMPORTANT → Allow preflight requests
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                    // Public endpoints - Auth endpoints for signup/login
                    .requestMatchers("/api/auth/**").permitAll()

                    // Public read-only endpoints
                    .requestMatchers(HttpMethod.GET, "/api/coding/problems").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/coding/problems/**").permitAll()

                    // Admin endpoints - Require ADMIN role
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")

                    // All other endpoints require authentication
                    .anyRequest().authenticated()
            );

        // ✅ Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}