package io.github.raphaelmun1z.ecommerce.dtos.security;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;

@Schema(description = "Objeto contendo tokens de acesso e refresh para autenticação")
public record TokenDTO(

    @Schema(description = "Nome de usuário autenticado", example = "usuario@email.com")
    String username,

    @Schema(description = "Indica se a autenticação foi bem-sucedida", example = "true")
    Boolean authenticated,

    @Schema(description = "Data e hora da criação do token")
    Date created,

    @Schema(description = "Data e hora de expiração do token de acesso")
    Date expiration,

    @Schema(description = "Token JWT para acesso aos recursos protegidos")
    String accessToken,

    @Schema(description = "Token para renovação do acesso sem necessidade de novo login")
    String refreshToken
) implements Serializable {
}