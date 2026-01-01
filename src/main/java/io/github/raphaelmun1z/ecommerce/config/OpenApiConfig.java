package io.github.raphaelmun1z.ecommerce.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Ecommerce API", version = "v1", description = "Documentação da API RESTful para o sistema Ecommerce.", termsOfService = "https://nexus-sistemas.github.io/Portfolio-Nexus/inicio", license = @License(name = "Apache 2.0", url = "https://nexus-sistemas.github.io/Portfolio-Nexus/inicio")))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {

}