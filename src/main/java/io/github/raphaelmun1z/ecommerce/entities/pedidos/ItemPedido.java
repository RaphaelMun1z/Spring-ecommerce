package io.github.raphaelmun1z.ecommerce.entities.pedidos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
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