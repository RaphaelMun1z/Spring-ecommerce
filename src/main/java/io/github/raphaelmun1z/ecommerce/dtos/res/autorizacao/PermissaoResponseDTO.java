package io.github.raphaelmun1z.ecommerce.dtos.res.autorizacao;


import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Permissao;

public record PermissaoResponseDTO(
        String id,
        String nome
) {
    public PermissaoResponseDTO(Permissao permissao) {
        this(permissao.getId(), permissao.getNome());
    }
}