package io.github.raphaelmun1z.ecommerce.entities.estoque;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoMovimentacao;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_movimentacao_estoque", indexes = {
    @Index(name = "idx_mov_estoque_produto", columnList = "produto_id"),
    @Index(name = "idx_mov_estoque_data", columnList = "data_movimentacao")
})
@Schema(description = "Entidade que registra o histórico de entradas e saídas de produtos no estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da movimentação", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @NotNull(message = "O produto é obrigatório.")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @Schema(description = "Produto que sofreu a movimentação")
    private Produto produto;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade movimentada deve ser pelo menos 1.")
    @Column(nullable = false)
    @Schema(description = "Quantidade de itens movimentados (sempre positivo)", example = "10")
    private Integer quantidade;

    @NotNull(message = "O tipo de movimentação é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", nullable = false, length = 20)
    @Schema(description = "Tipo da operação (ENTRADA ou SAIDA)", example = "ENTRADA")
    private TipoMovimentacao tipo;

    @Column(length = 100)
    @Schema(description = "Motivo ou justificativa da movimentação", example = "Compra de fornecedor NF-1234")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @Schema(description = "Pedido associado, caso seja uma venda ou devolução")
    private Pedido pedido;

    @CreationTimestamp
    @Column(name = "data_movimentacao", nullable = false, updatable = false)
    @Schema(description = "Data e hora exata em que a movimentação ocorreu")
    private LocalDateTime dataMovimentacao;

    public MovimentacaoEstoque() {
    }

    public MovimentacaoEstoque(Produto produto, Integer quantidade, TipoMovimentacao tipo, String motivo) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.motivo = motivo;
    }

    public String getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }
}