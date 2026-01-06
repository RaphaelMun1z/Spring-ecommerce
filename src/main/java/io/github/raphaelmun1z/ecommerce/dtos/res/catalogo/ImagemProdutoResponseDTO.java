package io.github.raphaelmun1z.ecommerce.dtos.res.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.ImagemProduto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO contendo os detalhes da imagem do produto")
public record ImagemProdutoResponseDTO(
    @Schema(description = "ID da imagem")
    String id,

    @Schema(description = "URL pública da imagem")
    String url,

    @Schema(description = "Ordem de exibição")
    Integer ordem,

    @Schema(description = "Se é a imagem principal")
    Boolean principal
) {
    public ImagemProdutoResponseDTO(ImagemProduto imagem) {
        this(imagem.getId(), imagem.getUrl(), imagem.getOrdem(), imagem.getPrincipal());
    }
}