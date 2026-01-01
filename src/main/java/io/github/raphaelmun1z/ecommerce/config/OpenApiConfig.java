package io.github.raphaelmun1z.ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Value("${spring.application.version:v1}")
    private String appVersion;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Ecommerce API")
                .version(appVersion)
                .description("Documentação da API RESTful para o sistema Ecommerce.")
                .termsOfService("https://nexus-sistemas.github.io/Portfolio-Nexus/inicio")
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://nexus-sistemas.github.io/Portfolio-Nexus/inicio")))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Insira o token JWT aqui para autenticar as requisições.")));
    }
}