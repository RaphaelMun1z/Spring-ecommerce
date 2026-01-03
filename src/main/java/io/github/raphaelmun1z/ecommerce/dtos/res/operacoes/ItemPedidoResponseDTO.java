package io.github.raphaelmun1z.ecommerce.dtos.res.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.pedidos.ItemPedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO representando um item consolidado dentro de um pedido")
public class ItemPedidoResponseDTO {

    @Schema(description = "Nome do produto", example = "Smartphone Galaxy S23")
    private String nomeProduto;

    @Schema(description = "Quantidade comprada", example = "1")
    private Integer quantidade;

    @Schema(description = "Preço unitário registrado no momento da compra", example = "4500.00")
    private BigDecimal precoUnitario;

    @Schema(description = "Subtotal do item", example = "4500.00")
    private BigDecimal subTotal;

    public ItemPedidoResponseDTO() {
    }

    public ItemPedidoResponseDTO(ItemPedido item) {
        this.nomeProduto = item.getProduto() != null ? item.getProduto().getTitulo() : "Produto Removido";
        this.quantidade = item.getQuantidade();
        this.precoUnitario = item.getPrecoUnitario();
        this.subTotal = item.getSubTotal();
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

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }
}