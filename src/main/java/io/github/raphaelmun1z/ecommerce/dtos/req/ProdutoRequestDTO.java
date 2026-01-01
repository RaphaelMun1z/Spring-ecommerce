package io.github.raphaelmun1z.ecommerce.dtos.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Objeto de transporte para criação ou atualização de um produto")
public class ProdutoRequestDTO {

    @Schema(description = "Código único de controle (SKU) do produto", example = "FER-001-BOSCH")
    @NotBlank(message = "O código de controle (SKU) é obrigatório")
    private String codigoControle;

    @Schema(description = "Nome comercial do produto", example = "Furadeira de Impacto 650W")
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 150)
    private String titulo;

    @Schema(description = "Descrição detalhada técnica e comercial", example = "Furadeira ideal para concreto e madeira, com velocidade variável...")
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @Schema(description = "Preço base de venda", example = "299.90")
    @NotNull(message = "O preço é obrigatório")
    @Positive
    private BigDecimal preco;

    @Schema(description = "Preço promocional (deve ser menor que o preço base)", example = "249.90", nullable = true)
    @PositiveOrZero
    private BigDecimal precoPromocional;

    @Schema(description = "Quantidade física disponível em estoque", example = "50")
    @NotNull(message = "O estoque é obrigatório")
    @Min(0)
    private Integer estoque;

    @Schema(description = "Define se o produto estará visível na vitrine", example = "true", defaultValue = "true")
    private Boolean ativo;

    @Schema(description = "Peso do produto em Quilogramas (para cálculo de frete)", example = "1.5")
    private Double pesoKg;

    @Schema(description = "Dimensões físicas (Altura x Largura x Profundidade)", example = "30x10x20")
    private String dimensoes;

    @Schema(description = "ID da categoria à qual o produto pertence", example = "c56a4180-65aa-42ec-a945-5fd21dec0538")
    private String categoriaId;

    public ProdutoRequestDTO() {
    }

    // Getters e Setters
    public String getCodigoControle() {
        return codigoControle;
    }

    public void setCodigoControle(String codigoControle) {
        this.codigoControle = codigoControle;
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

    public Double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(Double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public String getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(String dimensoes) {
        this.dimensoes = dimensoes;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }
}