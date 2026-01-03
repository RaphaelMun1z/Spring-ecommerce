package io.github.raphaelmun1z.ecommerce.dtos.res.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.enums.MetodoPagamento;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPagamento;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pagamento;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO contendo as informações detalhadas do pagamento do pedido")
public class PagamentoResponseDTO {

    @Schema(description = "Identificador do pagamento (mesmo ID do pedido)", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Data e hora do processamento")
    private LocalDateTime dataPagamento;

    @Schema(description = "Status atual do pagamento", example = "APROVADO")
    private StatusPagamento status;

    @Schema(description = "Método utilizado", example = "CARTAO_CREDITO")
    private MetodoPagamento metodo;

    @Schema(description = "Valor pago", example = "150.00")
    private BigDecimal valor;

    @Schema(description = "Número de parcelas", example = "3")
    private Integer numeroParcelas;

    @Schema(description = "Código da transação no gateway", example = "txn_1234567890")
    private String codigoTransacaoGateway;

    @Schema(description = "ID do pedido associado", example = "550e8400-e29b-41d4-a716-446655440000")
    private String pedidoId;

    public PagamentoResponseDTO() {
    }

    public PagamentoResponseDTO(Pagamento pagamento) {
        this.id = pagamento.getId();
        this.dataPagamento = pagamento.getDataPagamento();
        this.status = pagamento.getStatus();
        this.metodo = pagamento.getMetodo();
        this.valor = pagamento.getValor();
        this.numeroParcelas = pagamento.getNumeroParcelas();
        this.codigoTransacaoGateway = pagamento.getCodigoTransacaoGateway();
        this.pedidoId = pagamento.getPedido() != null ? pagamento.getPedido().getId() : null;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPagamento metodo) {
        this.metodo = metodo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(Integer numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public String getCodigoTransacaoGateway() {
        return codigoTransacaoGateway;
    }

    public void setCodigoTransacaoGateway(String codigoTransacaoGateway) {
        this.codigoTransacaoGateway = codigoTransacaoGateway;
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }
}