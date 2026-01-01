package io.github.raphaelmun1z.ecommerce.dtos.res.autorizacao;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;

public record ClienteResponseDTO(
    String id,
    String nome,
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
