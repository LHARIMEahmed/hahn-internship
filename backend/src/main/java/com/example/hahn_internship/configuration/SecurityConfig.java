package com.example.hahn_internship.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.hahn_internship.security.JwtAuthenticationFilter;

/**
 * Spring Security configuration class.
 * Configures authentication and authorization rules for the application.
 * Applies JWT filter to secure endpoints.
 */
@Configuration
public class SecurityConfig {

    /** 
     * JWT authentication filter that intercepts requests to validate JWT tokens.
     */
    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    /**
     * Configures the security filter chain for HTTP requests.
     * 
     * - Disables CSRF protection (useful for testing or API clients)
     * - Allows unauthenticated access to /auth/** endpoints (login/register)
     * - Requires authentication for all other requests
     * - Adds JWT filter before the UsernamePasswordAuthenticationFilter
     *
     * @param http the HttpSecurity object to configure
     * @return a configured SecurityFilterChain
     * @throws Exception if there is a configuration error
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF protection (not needed for stateless APIs)
            .csrf(csrf -> csrf.disable())
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // allow login/register endpoints
                .anyRequest().authenticated()           // all other endpoints require authentication
            )
            // Add JWT filter before Spring Security's username/password filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
