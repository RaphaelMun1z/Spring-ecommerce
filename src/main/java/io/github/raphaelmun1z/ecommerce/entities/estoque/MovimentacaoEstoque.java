package io.github.raphaelmun1z.ecommerce.entities.estoque;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoMovimentacao;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
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
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "O produto é obrigatório.")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade movimentada deve ser pelo menos 1.")
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull(message = "O tipo de movimentação é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", nullable = false, length = 20)
    private TipoMovimentacao tipo;

    @Column(length = 100)
    private String motivo;

    // Se a movimentação for oriunda de um pedido (venda ou devolução), linkamos aqui para rastreabilidade
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @CreationTimestamp
    @Column(name = "data_movimentacao", nullable = false, updatable = false)
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