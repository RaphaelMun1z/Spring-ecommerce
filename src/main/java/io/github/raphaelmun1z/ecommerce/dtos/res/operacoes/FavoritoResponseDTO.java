package io.github.raphaelmun1z.ecommerce.dtos.res.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.carrinho.Favorito;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.ImagemProduto;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO de resposta para itens favoritos, contendo dados completos para exibição em cards")
public class FavoritoResponseDTO {

    @Schema(description = "ID do registro de favorito", example = "fav-uuid-123")
    private String id;

    @Schema(description = "ID do produto", example = "prod-uuid-456")
    private String produtoId;

    @Schema(description = "SKU do produto", example = "FER-001")
    private String codigoControle;

    @Schema(description = "Título do produto", example = "Furadeira de Impacto")
    private String titulo;

    @Schema(description = "Descrição curta do produto", example = "Ideal para concreto...")
    private String descricao;

    @Schema(description = "Nome da categoria", example = "Ferramentas")
    private String categoria;

    @Schema(description = "Preço atual (considerando promoção)", example = "299.90")
    private BigDecimal preco;

    @Schema(description = "Preço original (se houver promoção)", example = "350.00")
    private BigDecimal precoOriginal;

    @Schema(description = "Indica se há estoque disponível", example = "true")
    private Boolean emEstoque;

    @Schema(description = "Lista de URLs das imagens do produto")
    private List<String> imagens = new ArrayList<>();

    @Schema(description = "Data em que foi favoritado")
    private LocalDateTime dataAdicao;

    public FavoritoResponseDTO(Favorito favorito) {
        this.id = favorito.getId();
        this.dataAdicao = favorito.getDataAdicao();

        Produto p = favorito.getProduto();
        if (p != null) {
            this.produtoId = p.getId();
            this.codigoControle = p.getCodigoControle();
            this.titulo = p.getTitulo();
            this.descricao = p.getDescricao();
            this.categoria = p.getCategoria() != null ? p.getCategoria().getNome() : "Geral";

            // Lógica de Preço
            if (p.getPrecoPromocional() != null) {
                this.preco = p.getPrecoPromocional();
                this.precoOriginal = p.getPreco(); // Envia o original para mostrar o "de: R$ X"
            } else {
                this.preco = p.getPreco();
                this.precoOriginal = null;
            }

            this.emEstoque = p.getEstoque() > 0;

            // Mapeia as imagens ordenadas
            if (p.getImagens() != null && !p.getImagens().isEmpty()) {
                this.imagens = p.getImagens().stream()
                    .sorted(Comparator.comparingInt(ImagemProduto::getOrdem))
                    .map(ImagemProduto::getUrl)
                    .collect(Collectors.toList());
            }
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getProdutoId() {
        return produtoId;
    }

    public String getCodigoControle() {
        return codigoControle;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public BigDecimal getPrecoOriginal() {
        return precoOriginal;
    }

    public Boolean getEmEstoque() {
        return emEstoque;
    }

    public List<String> getImagens() {
        return imagens;
    }

    public LocalDateTime getDataAdicao() {
        return dataAdicao;
    }
}