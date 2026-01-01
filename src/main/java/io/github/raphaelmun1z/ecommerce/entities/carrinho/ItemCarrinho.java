package io.github.raphaelmun1z.ecommerce.entities.carrinho;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_item_carrinho")
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Carrinho carrinho;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
    @Column(nullable = false)
    private Integer quantidade;

    public ItemCarrinho() {
    }

    public ItemCarrinho(Carrinho carrinho, Produto produto, Integer quantidade) {
        this.carrinho = carrinho;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    @Transient
    public BigDecimal getSubTotal() {
        if (produto == null || produto.getPreco() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal precoFinal = produto.getPrecoPromocional() != null
            ? produto.getPrecoPromocional()
            : produto.getPreco();

        return precoFinal.multiply(new BigDecimal(quantidade));
    }

    public String getId() {
        return id;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
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
}