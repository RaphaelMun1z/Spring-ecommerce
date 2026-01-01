package io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record PapelRequestDTO(
        @NotBlank(message = "O nome do papel é obrigatório.")
        String nome,

        @NotEmpty(message = "É necessário fornecer ao menos um ID de permissão.")
        Set<String> permissoesIds
) {}
