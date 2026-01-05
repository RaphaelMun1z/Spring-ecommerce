package io.github.raphaelmun1z.ecommerce.dtos.req.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "DTO para criação e atualização de produtos")
public class ProdutoRequestDTO {

    @NotBlank(message = "O código de controle (SKU) é obrigatório.")
    @Schema(description = "SKU único", example = "FER-001-BOSCH")
    private String codigoControle;

    @NotBlank(message = "O título é obrigatório.")
    @Size(min = 3, max = 150)
    @Schema(description = "Nome do produto", example = "Furadeira de Impacto")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória.")
    @Schema(description = "Detalhes do produto", example = "Descrição detalhada...")
    private String descricao;

    @NotNull(message = "O preço é obrigatório.")
    @Positive
    @Schema(description = "Preço padrão", example = "299.90")
    private BigDecimal preco;

    @Schema(description = "Preço promocional", example = "249.90")
    private BigDecimal precoPromocional;

    @NotNull
    @Min(0)
    @Schema(description = "Quantidade inicial em estoque", example = "50")
    private Integer estoque;

    @Schema(description = "Define se o produto já nasce ativo", example = "true")
    private Boolean ativo = true;

    @Schema(description = "Peso em KG", example = "1.5")
    private Double pesoKg;

    @Schema(description = "Dimensões (AxLxP)", example = "30x10x20")
    private String dimensoes;

    @Schema(description = "ID da categoria vinculada", example = "550e8400-e29b-41d4-a716-446655440000")
    private String categoriaId;

    @Schema(description = "Lista de URLs das imagens do produto", example = "[\"https://.../img1.jpg\", \"https://.../img2.jpg\"]")
    private List<String> imagens;

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

    public List<String> getImagens() {
        return imagens;
    }

    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }
}