package com.biblioteca.c4.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Biblioteca C4 Java API")
                .description("API REST para gerenciamento de livros, usuarios e emprestimos")
                .version("v1")
                .license(new License().name("MIT")));
    }
}
