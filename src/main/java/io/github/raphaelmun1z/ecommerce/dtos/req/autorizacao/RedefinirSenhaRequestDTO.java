package io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RedefinirSenhaRequestDTO(
        @NotBlank(message = "O token de redefinição é obrigatório.")
        String token,

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres.")
        String novaSenha
) {}