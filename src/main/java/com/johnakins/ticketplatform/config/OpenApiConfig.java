package com.johnakins.ticketplatform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ticketPlatformOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ticket Platform API")
                        .description(
                                "API for event discovery, ticket reservations, "
                                        + "payments, ticket generation and verification."
                        )
                        .version("1.0.0"));
    }
}
