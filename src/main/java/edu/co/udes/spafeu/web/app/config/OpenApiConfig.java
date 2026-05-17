package edu.co.udes.spafeu.web.app.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("SPAFEU API")
                        .description("API del Sistema de Preparación para la Actividad Física (SPAFEU) "
                                + "de la Universidad de Santander (UDES). Proporciona endpoints para la gestión "
                                + "de usuarios, cuestionarios de salud, registro de actividad física y "
                                + "autenticación mediante JWT.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Desarrollo SPAFEU - UDES")
                                .email("spafeu@udes.edu.co")
                                .url("https://udes.edu.co"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8000")
                                .description("Servidor local de desarrollo")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH,
                                new SecurityScheme()
                                        .name(BEARER_AUTH)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(In.HEADER)
                                        .description("Token JWT obtenido del endpoint de autenticación")));
    }
}