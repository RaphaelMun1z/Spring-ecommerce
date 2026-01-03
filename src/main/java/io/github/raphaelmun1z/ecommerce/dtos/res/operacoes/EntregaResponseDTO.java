package io.github.raphaelmun1z.ecommerce.dtos.res.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEntrega;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Entrega;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO contendo os dados de rastreamento e logística da entrega")
public class EntregaResponseDTO {

    @Schema(description = "Identificador da entrega", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Status atual da entrega", example = "ENVIADO")
    private StatusEntrega status;

    @Schema(description = "Código de rastreio", example = "AB123456789BR")
    private String codigoRastreio;

    @Schema(description = "Transportadora responsável", example = "Correios")
    private String transportadora;

    @Schema(description = "Valor do frete", example = "25.90")
    private BigDecimal valorFrete;

    @Schema(description = "Prazo estimado em dias úteis", example = "5")
    private Integer prazoDiasUteis;

    @Schema(description = "Data estimada para a entrega")
    private LocalDateTime dataEstimadaEntrega;

    @Schema(description = "Data do envio")
    private LocalDateTime dataEnvio;

    @Schema(description = "Data da entrega realizada")
    private LocalDateTime dataEntregaReal;

    @Schema(description = "CEP do destino", example = "01001-000")
    private String cep;

    @Schema(description = "Logradouro", example = "Av. Paulista")
    private String logradouro;

    @Schema(description = "Número", example = "1000")
    private String numero;

    @Schema(description = "Complemento", example = "Apto 101")
    private String complemento;

    @Schema(description = "Bairro", example = "Bela Vista")
    private String bairro;

    @Schema(description = "Cidade", example = "São Paulo")
    private String cidade;

    @Schema(description = "Estado (UF)", example = "SP")
    private String uf;

    public EntregaResponseDTO() {
    }

    public EntregaResponseDTO(Entrega entrega) {
        this.id = entrega.getId();
        this.status = entrega.getStatus();
        this.codigoRastreio = entrega.getCodigoRastreio();
        this.transportadora = entrega.getTransportadora();
        this.valorFrete = entrega.getValorFrete();
        this.prazoDiasUteis = entrega.getPrazoDiasUteis();
        this.dataEstimadaEntrega = entrega.getDataEstimadaEntrega();
        this.dataEnvio = entrega.getDataEnvio();
        this.dataEntregaReal = entrega.getDataEntregaReal();
        this.cep = entrega.getCep();
        this.logradouro = entrega.getLogradouro();
        this.numero = entrega.getNumero();
        this.complemento = entrega.getComplemento();
        this.bairro = entrega.getBairro();
        this.cidade = entrega.getCidade();
        this.uf = entrega.getUf();
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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