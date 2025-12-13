package com.example.hahn_internship.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())   // Désactive CSRF pour les tests
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()  // Autorise login/register
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
