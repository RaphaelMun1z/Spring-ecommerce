package io.github.raphaelmun1z.ecommerce.dtos.res.autorizacao;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta com dados básicos do cliente para fins de autorização")
public record ClienteResponseDTO(

    @Schema(description = "Identificador único do cliente", example = "550e8400-e29b-41d4-a716-446655440000")
    String id,

    @Schema(description = "Nome completo do cliente", example = "João da Silva")
    String nome,

    @Schema(description = "Endereço de email do cliente", example = "joao@email.com")
    String email
) {
    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        return new ClienteResponseDTO(
            cliente.getId(),
            cliente.getNome(),
            cliente.getEmail()
        );
    }
}