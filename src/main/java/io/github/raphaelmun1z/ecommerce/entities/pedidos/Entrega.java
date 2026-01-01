package io.github.raphaelmun1z.ecommerce.entities.pedidos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEntrega;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_entrega", indexes = {
    @Index(name = "idx_entrega_codigo_rastreio", columnList = "codigo_rastreio")
})
@Schema(description = "Entidade que representa os dados logísticos e de entrega de um pedido")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da entrega", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    @JsonIgnore
    @Schema(description = "Pedido associado a esta entrega")
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Status atual da entrega", example = "ENVIADO")
    private StatusEntrega status = StatusEntrega.PENDENTE;

    @Column(name = "codigo_rastreio", length = 50)
    @Schema(description = "Código de rastreio da transportadora", example = "AB123456789BR")
    private String codigoRastreio;

    @Column(length = 100)
    @Schema(description = "Nome da transportadora responsável", example = "Correios")
    private String transportadora;

    @NotNull
    @Column(name = "valor_frete", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Valor cobrado pelo frete", example = "25.90")
    private BigDecimal valorFrete;

    @Column(name = "prazo_dias_uteis")
    @Schema(description = "Prazo estimado em dias úteis para a entrega", example = "5")
    private Integer prazoDiasUteis;

    @Column(name = "data_estimada_entrega")
    @Schema(description = "Data prevista para a entrega chegar ao cliente")
    private LocalDateTime dataEstimadaEntrega;

    @Column(name = "data_envio")
    @Schema(description = "Data em que o pedido foi despachado")
    private LocalDateTime dataEnvio;

    @Column(name = "data_entrega_real")
    @Schema(description = "Data em que o pedido foi efetivamente entregue")
    private LocalDateTime dataEntregaReal;

    @NotNull
    @Size(max = 9)
    @Column(nullable = false, length = 9)
    @Schema(description = "CEP do endereço de entrega", example = "01001-000")
    private String cep;

    @NotNull
    @Column(nullable = false)
    @Schema(description = "Logradouro (Rua, Avenida, etc.)", example = "Av. Paulista")
    private String logradouro;

    @NotNull
    @Column(nullable = false, length = 20)
    @Schema(description = "Número do endereço", example = "1000")
    private String numero;

    @Schema(description = "Complemento do endereço (opcional)", example = "Apto 101")
    private String complemento;

    @NotNull
    @Column(nullable = false)
    @Schema(description = "Bairro", example = "Bela Vista")
    private String bairro;

    @NotNull
    @Column(nullable = false)
    @Schema(description = "Cidade", example = "São Paulo")
    private String cidade;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(nullable = false, length = 2)
    @Schema(description = "Unidade Federativa (UF)", example = "SP")
    private String uf;

    public Entrega() {
    }

    public Entrega(Pedido pedido, StatusEntrega status, String codigoRastreio, String transportadora, BigDecimal valorFrete, Integer prazoDiasUteis, LocalDateTime dataEstimadaEntrega, LocalDateTime dataEnvio, LocalDateTime dataEntregaReal, String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String uf) {
        this.pedido = pedido;
        this.status = status;
        this.codigoRastreio = codigoRastreio;
        this.transportadora = transportadora;
        this.valorFrete = valorFrete;
        this.prazoDiasUteis = prazoDiasUteis;
        this.dataEstimadaEntrega = dataEstimadaEntrega;
        this.dataEnvio = dataEnvio;
        this.dataEntregaReal = dataEntregaReal;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
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

    public StatusEntrega getStatus() {
        return status;
    }

    public void setStatus(StatusEntrega status) {
        this.status = status;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public Integer getPrazoDiasUteis() {
        return prazoDiasUteis;
    }

    public void setPrazoDiasUteis(Integer prazoDiasUteis) {
        this.prazoDiasUteis = prazoDiasUteis;
    }

    public LocalDateTime getDataEstimadaEntrega() {
        return dataEstimadaEntrega;
    }

    public void setDataEstimadaEntrega(LocalDateTime dataEstimadaEntrega) {
        this.dataEstimadaEntrega = dataEstimadaEntrega;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public LocalDateTime getDataEntregaReal() {
        return dataEntregaReal;
    }

    public void setDataEntregaReal(LocalDateTime dataEntregaReal) {
        this.dataEntregaReal = dataEntregaReal;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}