package io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto de requisição para redefinição de senha utilizando um token")
public record RedefinirSenhaRequestDTO(

    @Schema(description = "Token de redefinição enviado por e-mail", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    @NotBlank(message = "O token de redefinição é obrigatório.")
    String token,

    @Schema(description = "Nova senha escolhida pelo usuário", example = "NovaSenhaForte@123")
    @NotBlank(message = "A nova senha é obrigatória.")
    @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres.")
    String novaSenha
) {
}