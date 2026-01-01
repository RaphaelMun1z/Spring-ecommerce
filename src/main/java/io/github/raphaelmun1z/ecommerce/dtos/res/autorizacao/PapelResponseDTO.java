package io.github.raphaelmun1z.ecommerce.dtos.res.autorizacao;

import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Papel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "Objeto de resposta contendo os dados de um papel (role) do sistema e suas permissões")
public record PapelResponseDTO(

    @Schema(description = "Identificador único do papel", example = "1")
    String id,

    @Schema(description = "Nome do papel", example = "ROLE_ADMIN")
    String nome,

    @Schema(description = "Conjunto de permissões associadas a este papel")
    Set<PermissaoResponseDTO> permissoes
) {
    public static PapelResponseDTO fromEntity(Papel papel) {
        if (papel == null) {
            return null;
        }

        Set<PermissaoResponseDTO> permissoesDTO = papel.getPermissoes() != null
            ? papel.getPermissoes().stream()
            .map(PermissaoResponseDTO::new)
            .collect(Collectors.toSet())
            : Collections.emptySet();

        return new PapelResponseDTO(
            papel.getId(),
            papel.getNome(),
            permissoesDTO
        );
    }
}