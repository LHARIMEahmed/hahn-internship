package com.example.hahn_internship.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to set up global CORS (Cross-Origin Resource Sharing) settings
 * for the Spring Boot backend. This ensures the frontend (React app) can communicate
 * with the backend without CORS issues.
 */
@Configuration
public class WebConfig {

    /**
     * Configure CORS mappings for the application.
     * 
     * @param registry the CorsRegistry to add mappings to
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // toutes les routes
                        .allowedOrigins("http://localhost:5173") // frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);
            }
        };
    }
}

