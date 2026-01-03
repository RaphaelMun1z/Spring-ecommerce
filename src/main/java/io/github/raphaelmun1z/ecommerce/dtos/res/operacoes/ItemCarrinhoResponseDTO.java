package io.github.raphaelmun1z.ecommerce.dtos.res.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.carrinho.ItemCarrinho;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO responsável por apresentar os dados resumidos de um item dentro do carrinho")
public class ItemCarrinhoResponseDTO {

    @Schema(description = "Identificador do item no carrinho", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Nome do produto adicionado", example = "Smartphone Galaxy S23")
    private String nomeProduto;

    @Schema(description = "Quantidade do produto", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço unitário do produto no momento da consulta (considera promoções)", example = "4500.00")
    private BigDecimal precoUnitario;

    @Schema(description = "Subtotal do item (preço x quantidade)", example = "9000.00")
    private BigDecimal subTotal;

    public ItemCarrinhoResponseDTO() {
    }

    public ItemCarrinhoResponseDTO(ItemCarrinho item) {
        this.id = item.getId();
        this.quantidade = item.getQuantidade();
        this.subTotal = item.getSubTotal();

        if (item.getProduto() != null) {
            this.nomeProduto = item.getProduto().getTitulo();

            this.precoUnitario = item.getProduto().getPrecoPromocional() != null
                ? item.getProduto().getPrecoPromocional()
                : item.getProduto().getPreco();
        } else {
            this.nomeProduto = "Produto Indisponível";
            this.precoUnitario = BigDecimal.ZERO;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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