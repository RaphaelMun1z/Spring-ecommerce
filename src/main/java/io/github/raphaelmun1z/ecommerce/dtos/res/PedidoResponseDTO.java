package io.github.raphaelmun1z.ecommerce.dtos.res;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.ItemPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "Objeto de resposta com os detalhes completos de um pedido realizado")
public class PedidoResponseDTO {

    @Schema(description = "Identificador único do pedido", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Data e hora em que o pedido foi realizado")
    private LocalDateTime dataPedido;

    @Schema(description = "Status atual do ciclo de vida do pedido", example = "PAGO")
    private StatusPedido status;

    @Schema(description = "Valor total do pedido (soma dos itens + frete - descontos)", example = "1250.90")
    private BigDecimal valorTotal;

    @Schema(description = "Nome do cliente que realizou o pedido", example = "Ana Clara")
    private String nomeCliente;

    @Schema(description = "E-mail de contato do cliente", example = "ana.c@email.com")
    private String emailCliente;

    @Schema(description = "URL do avatar do cliente (se disponível)", example = "https://ui-avatars.com/api/?name=Ana+C")
    private String avatarCliente;

    @Schema(description = "Lista detalhada dos itens comprados")
    private List<ItemPedidoDTO> itens;

    public PedidoResponseDTO(Pedido entity) {
        this.id = entity.getId();
        this.dataPedido = entity.getDataPedido();
        this.status = entity.getStatus();
        this.valorTotal = entity.getValorTotal();

        if (entity.getCliente() != null) {
            this.nomeCliente = entity.getCliente().getNome();
            this.emailCliente = entity.getCliente().getEmail();
            // this.avatarCliente = entity.getCliente().getAvatar();
        }

        this.itens = entity.getItens().stream()
            .map(ItemPedidoDTO::new)
            .collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getAvatarCliente() {
        return avatarCliente;
    }

    public void setAvatarCliente(String avatarCliente) {
        this.avatarCliente = avatarCliente;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }

    @Schema(description = "Detalhes resumidos de um item (produto) dentro do pedido")
    public static class ItemPedidoDTO {

        @Schema(description = "Nome/Título do produto comprado", example = "Furadeira Bosch")
        private String produtoNome;

        @Schema(description = "Quantidade adquirida", example = "1")
        private Integer quantidade;

        @Schema(description = "Preço unitário no momento da compra (snapshot)", example = "450.00")
        private BigDecimal precoUnitario;

        @Schema(description = "Subtotal do item (quantidade * preço unitário)", example = "450.00")
        private BigDecimal subTotal;

        public ItemPedidoDTO(ItemPedido item) {
            this.produtoNome = item.getProduto().getTitulo();
            this.quantidade = item.getQuantidade();
            this.precoUnitario = item.getPrecoUnitario();
            this.subTotal = item.getSubTotal();
        }

        public String getProdutoNome() {
            return produtoNome;
        }

        public void setProdutoNome(String produtoNome) {
            this.produtoNome = produtoNome;
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
}