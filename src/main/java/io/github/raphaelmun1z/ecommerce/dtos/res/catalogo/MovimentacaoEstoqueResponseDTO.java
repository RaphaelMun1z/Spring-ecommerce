package io.github.raphaelmun1z.ecommerce.dtos.res.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.enums.TipoMovimentacao;
import io.github.raphaelmun1z.ecommerce.entities.estoque.MovimentacaoEstoque;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO contendo os detalhes de uma movimentação de estoque")
public class MovimentacaoEstoqueResponseDTO {

    @Schema(description = "ID da movimentação", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "ID do produto", example = "550e8400-e29b-41d4-a716-446655440000")
    private String produtoId;

    @Schema(description = "Nome do produto", example = "Furadeira de Impacto")
    private String nomeProduto;

    @Schema(description = "Quantidade movimentada", example = "10")
    private Integer quantidade;

    @Schema(description = "Tipo da movimentação (ENTRADA/SAIDA)", example = "ENTRADA")
    private TipoMovimentacao tipo;

    @Schema(description = "Motivo da movimentação", example = "Recebimento NF-1234")
    private String motivo;

    @Schema(description = "ID do pedido associado (se houver)", example = "550e8400-e29b-41d4-a716-446655440000")
    private String pedidoId;

    @Schema(description = "Data da movimentação")
    private LocalDateTime dataMovimentacao;

    public MovimentacaoEstoqueResponseDTO() {
    }

    public MovimentacaoEstoqueResponseDTO(MovimentacaoEstoque entity) {
        this.id = entity.getId();
        this.quantidade = entity.getQuantidade();
        this.tipo = entity.getTipo();
        this.motivo = entity.getMotivo();
        this.dataMovimentacao = entity.getDataMovimentacao();

        if (entity.getProduto() != null) {
            this.produtoId = entity.getProduto().getId();
            this.nomeProduto = entity.getProduto().getTitulo();
        }

        if (entity.getPedido() != null) {
            this.pedidoId = entity.getPedido().getId();
        }
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(String produtoId) {
        this.produtoId = produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
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

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }
}