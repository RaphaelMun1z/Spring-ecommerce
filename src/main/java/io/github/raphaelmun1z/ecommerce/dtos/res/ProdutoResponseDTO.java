package io.github.raphaelmun1z.ecommerce.dtos.res;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Objeto de resposta contendo os dados públicos do produto")
public class ProdutoResponseDTO {

    @Schema(description = "Identificador único do produto", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Título comercial do produto", example = "Furadeira de Impacto 650W")
    private String titulo;

    @Schema(description = "Descrição detalhada do produto", example = "Furadeira ideal para concreto e madeira...")
    private String descricao;

    @Schema(description = "Preço original de venda", example = "299.90")
    private BigDecimal preco;

    @Schema(description = "Preço promocional (se houver)", example = "249.90", nullable = true)
    private BigDecimal precoPromocional;

    @Schema(description = "Quantidade atual em estoque", example = "50")
    private Integer estoque;

    @Schema(description = "Indica se o produto está ativo para venda", example = "true")
    private Boolean ativo;

    @Schema(description = "Nome da categoria vinculada", example = "Ferramentas Elétricas", nullable = true)
    private String categoriaNome;

    public ProdutoResponseDTO() {
    }

    public ProdutoResponseDTO(Produto entity) {
        this.id = entity.getId();
        this.titulo = entity.getTitulo();
        this.descricao = entity.getDescricao();
        this.preco = entity.getPreco();
        this.precoPromocional = entity.getPrecoPromocional();
        this.estoque = entity.getEstoque();
        this.ativo = entity.getAtivo();
        // Assume que a entidade Produto pode ter a categoria nula ou o método getCategoria() implementado
        // this.categoriaNome = entity.getCategoria() != null ? entity.getCategoria().getNome() : null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getPrecoPromocional() {
        return precoPromocional;
    }

    public void setPrecoPromocional(BigDecimal precoPromocional) {
        this.precoPromocional = precoPromocional;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }
}