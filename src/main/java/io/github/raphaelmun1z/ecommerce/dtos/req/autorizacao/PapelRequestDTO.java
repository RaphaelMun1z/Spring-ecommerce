package io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Schema(description = "Objeto de requisição para criação ou atualização de um papel (role)")
public record PapelRequestDTO(

    @Schema(description = "Nome do papel", example = "ROLE_GERENTE")
    @NotBlank(message = "O nome do papel é obrigatório.")
    String nome,

    @Schema(description = "Lista de IDs das permissões associadas a este papel")
    @NotEmpty(message = "É necessário fornecer ao menos um ID de permissão.")
    Set<String> permissoesIds
) {
}