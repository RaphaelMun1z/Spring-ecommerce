package io.github.raphaelmun1z.ecommerce.dtos.req.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.enums.TipoMovimentacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para registro manual de movimentação de estoque")
public class MovimentacaoEstoqueRequestDTO {

    @NotBlank(message = "O ID do produto é obrigatório.")
    @Schema(description = "ID do produto a ser movimentado", example = "550e8400-e29b-41d4-a716-446655440000")
    private String produtoId;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    @Schema(description = "Quantidade a ser adicionada ou removida", example = "50")
    private Integer quantidade;

    @NotNull(message = "O tipo de movimentação é obrigatório.")
    @Schema(description = "Tipo (ENTRADA para reabastecimento, SAIDA para correções)", example = "ENTRADA")
    private TipoMovimentacao tipo;

    @Schema(description = "Motivo (ex: 'Compra Fornecedor', 'Ajuste Inventário')", example = "Ajuste de inventário anual")
    private String motivo;

    // Getters e Setters
    public String getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(String produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}