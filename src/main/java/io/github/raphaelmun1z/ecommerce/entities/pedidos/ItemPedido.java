package io.github.raphaelmun1z.ecommerce.entities.pedidos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_item_pedido")
@Schema(description = "Entidade que representa um item dentro de um pedido finalizado, mantendo o histórico de preço")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único do item do pedido", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @Schema(description = "Pedido ao qual este item pertence")
    private Pedido pedido;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "produto_id", nullable = false)
    @Schema(description = "Produto adquirido")
    private Produto produto;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
    @Column(nullable = false)
    @Schema(description = "Quantidade comprada", example = "2")
    private Integer quantidade;

    @NotNull
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Preço do produto no momento da compra (Snapshot para histórico)", example = "99.90")
    private BigDecimal precoUnitario;

    public ItemPedido() {
    }

    public ItemPedido(Pedido pedido, Produto produto, Integer quantidade, BigDecimal precoUnitario) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    @Transient
    @Schema(description = "Subtotal calculado (preço unitário * quantidade)", example = "199.80", accessMode = Schema.AccessMode.READ_ONLY)
    public BigDecimal getSubTotal() {
        if (precoUnitario == null || quantidade == null) {
            return BigDecimal.ZERO;
        }
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }

    public String getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
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

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}