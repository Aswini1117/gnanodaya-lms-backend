package com.gnanodaya.lms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gnanodaya VLSI LMS API")
                        .description("Complete REST API for Gnanodaya VLSI Institute LMS Portal.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gnanodaya VLSI Institute")
                                .email("info@gnanodaya.com")
                                .url("https://gnanodaya.com"))
                        .license(new License()
                                .name("Private")
                                .url("https://gnanodaya.com")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description(
                                                "Enter JWT token (without 'Bearer ' prefix)")));
    }
}