package com.mertncu.UniversityClubManagementFramework.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("University Club Management Framework API")
                .description("This is a Spring Boot RESTful service using SpringDoc OpenAPI documentation.")
                .contact(new Contact()
                        .name("Mert Uncu"))
                .version("1.0"));
    }
}
