package io.github.raphaelmun1z.ecommerce.dtos.res.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO completo com as informações do pedido, incluindo entrega, itens e pagamento")
public class PedidoResponseDTO {

    @Schema(description = "Identificador único do pedido", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Data da realização do pedido")
    private LocalDateTime dataPedido;

    @Schema(description = "Status atual do pedido", example = "AGUARDANDO_PAGAMENTO")
    private StatusPedido status;

    @Schema(description = "Valor total (inclui frete e descontos)", example = "350.00")
    private BigDecimal valorTotal;

    @Schema(description = "Valor total de descontos aplicados", example = "10.00")
    private BigDecimal valorDesconto;

    @Schema(description = "ID do cliente que realizou o pedido", example = "550e8400-e29b-41d4-a716-446655440000")
    private String clienteId;

    @Schema(description = "Informações detalhadas da entrega")
    private EntregaResponseDTO entrega;

    @Schema(description = "Informações detalhadas do pagamento")
    private PagamentoResponseDTO pagamento;

    @Schema(description = "Lista de itens comprados")
    private List<ItemPedidoResponseDTO> itens = new ArrayList<>();

    public PedidoResponseDTO() {
    }

    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.dataPedido = pedido.getDataPedido();
        this.status = pedido.getStatus();
        this.valorTotal = pedido.getValorTotal();
        this.valorDesconto = pedido.getValorDesconto();
        this.clienteId = pedido.getCliente() != null ? pedido.getCliente().getId() : null;

        if (pedido.getEntrega() != null) {
            this.entrega = new EntregaResponseDTO(pedido.getEntrega());
        }

        if (pedido.getPagamento() != null) {
            this.pagamento = new PagamentoResponseDTO(pedido.getPagamento());
        }

        if (pedido.getItens() != null) {
            this.itens = pedido.getItens().stream()
                .map(ItemPedidoResponseDTO::new)
                .collect(Collectors.toList());
        }
    }

    // Getters e Setters
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

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public EntregaResponseDTO getEntrega() {
        return entrega;
    }

    public void setEntrega(EntregaResponseDTO entrega) {
        this.entrega = entrega;
    }

    public PagamentoResponseDTO getPagamento() {
        return pagamento;
    }

    public void setPagamento(PagamentoResponseDTO pagamento) {
        this.pagamento = pagamento;
    }

    public List<ItemPedidoResponseDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoResponseDTO> itens) {
        this.itens = itens;
    }
}