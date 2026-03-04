package com.example.KindRaise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig {

    @Bean
     WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Apply this CORS rule to all API endpoints
                registry.addMapping("/**")
                // Allow requests only from this frontend.
                        .allowedOrigins("http://localhost:5173")
                        // This allows all HTTP methods.
                        .allowedMethods("*")
                        // This allows all request headers.
                        .allowedHeaders("*")
                        // This allows requests with credentials, such as:
                        .allowCredentials(true);
            }
        };
    }
}