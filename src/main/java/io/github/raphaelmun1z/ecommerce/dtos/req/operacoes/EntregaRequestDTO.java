package io.github.raphaelmun1z.ecommerce.dtos.req.operacoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "DTO para registro das informações de entrega")
public class EntregaRequestDTO {

    @Schema(description = "Código de rastreio (opcional na criação)", example = "AB123456789BR")
    private String codigoRastreio;

    @Schema(description = "Nome da transportadora", example = "Correios")
    private String transportadora;

    @NotNull(message = "O valor do frete é obrigatório.")
    @Schema(description = "Valor cobrado pelo frete", example = "25.90")
    private BigDecimal valorFrete;

    @Schema(description = "Prazo estimado em dias úteis", example = "5")
    private Integer prazoDiasUteis;

    @NotBlank(message = "O CEP é obrigatório.")
    @Size(max = 9)
    @Schema(description = "CEP do destino", example = "01001-000")
    private String cep;

    @NotBlank(message = "O logradouro é obrigatório.")
    @Schema(description = "Logradouro (Rua, Av.)", example = "Av. Paulista")
    private String logradouro;

    @NotBlank(message = "O número é obrigatório.")
    @Schema(description = "Número do endereço", example = "1000")
    private String numero;

    @Schema(description = "Complemento", example = "Apto 101")
    private String complemento;

    @NotBlank(message = "O bairro é obrigatório.")
    @Schema(description = "Bairro", example = "Bela Vista")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória.")
    @Schema(description = "Cidade", example = "São Paulo")
    private String cidade;

    @NotBlank(message = "A UF é obrigatória.")
    @Size(min = 2, max = 2)
    @Schema(description = "Estado (UF)", example = "SP")
    private String uf;

    // Getters e Setters
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