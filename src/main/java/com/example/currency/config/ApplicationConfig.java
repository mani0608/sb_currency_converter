package com.example.currency.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebMvc
@ComponentScan("com.example")
public class ApplicationConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed.endpoints}")
    private String origins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(origins.split(","))
                .allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS", "HEAD")
                .allowCredentials(false)
                .allowedHeaders("Content-Type", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Authorization")
                .maxAge(4800);
    }
}
