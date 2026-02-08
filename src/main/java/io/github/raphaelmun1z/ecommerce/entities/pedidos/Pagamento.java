package io.github.raphaelmun1z.ecommerce.entities.pedidos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.raphaelmun1z.ecommerce.entities.enums.MetodoPagamento;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pagamento")
@Schema(description = "Entidade que representa os dados de pagamento de um pedido")
public class Pagamento {

    @Id
    @Schema(description = "Identificador único do pagamento (corresponde ao ID do pedido)", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Column(name = "gateway", length = 30, nullable = false)
    private String gateway;

    @Column(name = "billing_id", length = 100, unique = true)
    private String billingId;

    @Column(name = "url_pagamento", length = 2048)
    @Schema(description = "URL para redirecionamento ao gateway de pagamento", example = "https://abacatepay.com/pay/...")
    private String urlPagamento;

    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Status atual do pagamento", example = "APROVADO")
    private StatusPagamento status = StatusPagamento.PENDENTE;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", nullable = false, length = 20)
    @Schema(description = "Método utilizado para pagamento", example = "CARTAO_CREDITO")
    private MetodoPagamento metodo;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "Valor total pago", example = "150.00")
    private BigDecimal valor;

    @Column(name = "numero_parcelas")
    @Schema(description = "Número de parcelas (se aplicável)", example = "3")
    private Integer numeroParcelas = 1;

    @Column(name = "codigo_transacao_gateway", length = 100)
    @Schema(description = "Código de transação retornado pelo gateway de pagamento", example = "txn_1234567890")
    private String codigoTransacaoGateway;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "pedido_id")
    @Schema(description = "Pedido associado ao pagamento")
    private Pedido pedido;

    public Pagamento() {
    }

    public Pagamento(String id, String gateway, String billingId, String urlPagamento, LocalDateTime dataConfirmacao, StatusPagamento status, MetodoPagamento metodo, BigDecimal valor, Integer numeroParcelas, String codigoTransacaoGateway, Pedido pedido) {
        this.id = id;
        this.gateway = gateway;
        this.billingId = billingId;
        this.urlPagamento = urlPagamento;
        this.dataConfirmacao = dataConfirmacao;
        this.status = status;
        this.metodo = metodo;
        this.valor = valor;
        this.numeroParcelas = numeroParcelas;
        this.codigoTransacaoGateway = codigoTransacaoGateway;
        this.pedido = pedido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getBillingId() {
        return billingId;
    }

    public void setBillingId(String billingId) {
        this.billingId = billingId;
    }

    public String getUrlPagamento() {
        return urlPagamento;
    }

    public void setUrlPagamento(String urlPagamento) {
        this.urlPagamento = urlPagamento;
    }

    public LocalDateTime getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(LocalDateTime dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}