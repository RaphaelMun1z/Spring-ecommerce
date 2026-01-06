package io.github.raphaelmun1z.ecommerce.dtos.req.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteUpdateRequestDTO(
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres.")
    String nome,

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
    String telefone
) {
}