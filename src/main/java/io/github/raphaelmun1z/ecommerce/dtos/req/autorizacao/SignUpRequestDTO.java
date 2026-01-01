package io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto de requisição para o registro de um novo usuário/cliente no sistema")
public record SignUpRequestDTO(

    @Schema(description = "Nome completo do usuário", example = "Carlos Souza")
    @NotBlank(message = "O nome é obrigatório.")
    String nome,

    @Schema(description = "Endereço de e-mail válido", example = "carlos@email.com")
    @Email(message = "O email fornecido é inválido.")
    @NotBlank(message = "O email é obrigatório.")
    String email,

    @Schema(description = "Senha de acesso (mínimo de 6 caracteres)", example = "senhaForte123")
    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    String senha
) {
}