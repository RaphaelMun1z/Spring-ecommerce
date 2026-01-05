package io.github.raphaelmun1z.ecommerce.dtos.res.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.carrinho.Favorito;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO de resposta para itens favoritos")
public class FavoritoResponseDTO {

    @Schema(description = "ID do registro de favorito", example = "fav-uuid-123")
    private String id;

    @Schema(description = "ID do produto", example = "prod-uuid-456")
    private String produtoId;

    @Schema(description = "Nome do produto", example = "Furadeira de Impacto")
    private String titulo;

    @Schema(description = "Nome da categoria", example = "Ferramentas")
    private String categoria;

    @Schema(description = "Preço atual", example = "299.90")
    private BigDecimal preco;

    @Schema(description = "Indica se há estoque disponível", example = "true")
    private Boolean emEstoque;

    @Schema(description = "Data em que foi favoritado")
    private LocalDateTime dataAdicao;

    // TODO: Adicionar URL da imagem quando o backend suportar upload
    // private String imagemUrl;

    public FavoritoResponseDTO(Favorito favorito) {
        this.id = favorito.getId();
        this.dataAdicao = favorito.getDataAdicao();

        Produto p = favorito.getProduto();
        if (p != null) {
            this.produtoId = p.getId();
            this.titulo = p.getTitulo();
            this.categoria = p.getCategoria() != null ? p.getCategoria().getNome() : "Geral";
            // Lógica de preço: Se tiver promoção, mostra a promoção, senão o preço cheio
            this.preco = p.getPrecoPromocional() != null ? p.getPrecoPromocional() : p.getPreco();
            this.emEstoque = p.getEstoque() > 0;
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getProdutoId() {
        return produtoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Boolean getEmEstoque() {
        return emEstoque;
    }

    public LocalDateTime getDataAdicao() {
        return dataAdicao;
    }
}