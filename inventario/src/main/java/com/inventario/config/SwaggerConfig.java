package com.inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API of Inventory gestion")
                .version("1.0")
                .description("System for the gestion of Products, Stock and Prices")
                .contact(new Contact().name("cgalean0").email("galeanochnahuel@gmail.com")));
    }
}
