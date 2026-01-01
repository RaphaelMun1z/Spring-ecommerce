package io.github.raphaelmun1z.ecommerce.dtos.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

@Schema(description = "Credenciais para autenticação de usuário")
public record AccountCredentialsDTO(

    @Schema(description = "E-mail do usuário cadastrado", example = "usuario@email.com")
    @NotBlank(message = "O email não pode ser vazio")
    @Email(message = "Formato de email inválido")
    String email,

    @Schema(description = "Senha de acesso", example = "MinhaSenhaForte123")
    @NotBlank(message = "A senha não pode ser vazia")
    String senha
) implements Serializable {
}