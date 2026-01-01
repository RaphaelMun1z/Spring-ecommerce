package io.github.raphaelmun1z.ecommerce.dtos.res.autorizacao;


import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Permissao;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta contendo os dados de uma permissão do sistema")
public record PermissaoResponseDTO(

    @Schema(description = "Identificador único da permissão", example = "1")
    String id,

    @Schema(description = "Nome da permissão", example = "ADMIN")
    String nome
) {
    public PermissaoResponseDTO(Permissao permissao) {
        this(permissao.getId(), permissao.getNome());
    }
}