package io.github.raphaelmun1z.ecommerce.dtos.req.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Filtros avançados para busca de produtos")
public class ProdutoFiltroDTO {

    @Schema(description = "Termo de busca (nome ou descrição)", example = "Gamer")
    private String termo;

    @Schema(description = "ID da categoria", example = "550e8400-e29b-41d4-a716-446655440000")
    private String categoriaId;

    @Schema(description = "Preço mínimo", example = "100.00")
    private BigDecimal precoMin;

    @Schema(description = "Preço máximo", example = "5000.00")
    private BigDecimal precoMax;

    @Schema(description = "Apenas produtos ativos?", example = "true", defaultValue = "true")
    private Boolean apenasAtivos = true;

    // Getters e Setters
    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }

    public BigDecimal getPrecoMin() {
        return precoMin;
    }

    public void setPrecoMin(BigDecimal precoMin) {
        this.precoMin = precoMin;
    }

    public BigDecimal getPrecoMax() {
        return precoMax;
    }

    public void setPrecoMax(BigDecimal precoMax) {
        this.precoMax = precoMax;
    }

    public Boolean getApenasAtivos() {
        return apenasAtivos;
    }

    public void setApenasAtivos(Boolean apenasAtivos) {
        this.apenasAtivos = apenasAtivos;
    }
}