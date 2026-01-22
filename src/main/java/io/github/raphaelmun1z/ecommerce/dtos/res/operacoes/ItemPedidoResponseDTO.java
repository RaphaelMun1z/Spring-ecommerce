package io.github.raphaelmun1z.ecommerce.dtos.res.operacoes;

import io.github.raphaelmun1z.ecommerce.dtos.res.catalogo.ImagemProdutoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.ImagemProduto;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.ItemPedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO representando um item consolidado dentro de um pedido")
public class ItemPedidoResponseDTO {

    @Schema(description = "ID do produto", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Nome do produto", example = "Smartphone Galaxy S23")
    private String nomeProduto;

    @Schema(description = "Quantidade comprada", example = "1")
    private Integer quantidade;

    @Schema(description = "Preço unitário registrado no momento da compra", example = "4500.00")
    private BigDecimal precoUnitario;

    @Schema(description = "Subtotal do item", example = "4500.00")
    private BigDecimal subTotal;

    @Schema(description = "Lista de objetos de imagem (com ordem e flag principal)")
    private List<ImagemProdutoResponseDTO> imagens;

    public ItemPedidoResponseDTO() {
    }

    public ItemPedidoResponseDTO(ItemPedido item) {
        this.id = item.getId();
        this.nomeProduto = item.getProduto() != null ? item.getProduto().getTitulo() : "Produto Removido";
        this.quantidade = item.getQuantidade();
        this.precoUnitario = item.getPrecoUnitario();
        this.subTotal = item.getSubTotal();

        // Inicializa a lista para evitar null
        this.imagens = new ArrayList<>();

        if (item.getProduto().getImagens() != null && !item.getProduto().getImagens().isEmpty()) {
            this.imagens = item.getProduto().getImagens().stream()
                    .sorted(Comparator.comparing(ImagemProduto::getOrdem, Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(ImagemProdutoResponseDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public List<ImagemProdutoResponseDTO> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImagemProdutoResponseDTO> imagens) {
        this.imagens = imagens;
    }
}