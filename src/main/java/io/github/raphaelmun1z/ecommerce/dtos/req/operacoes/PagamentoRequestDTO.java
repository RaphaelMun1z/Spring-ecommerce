package io.github.raphaelmun1z.ecommerce.dtos.req.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.enums.MetodoPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "DTO para registro de um novo pagamento")
public class PagamentoRequestDTO {

    @NotNull(message = "O método de pagamento é obrigatório.")
    @Schema(description = "Método escolhido", example = "CARTAO_CREDITO")
    private MetodoPagamento metodo;

    @NotNull(message = "O valor é obrigatório.")
    @Positive(message = "O valor deve ser positivo.")
    @Schema(description = "Valor a ser pago (deve corresponder ao total do pedido)", example = "150.00")
    private BigDecimal valor;

    @Schema(description = "Número de parcelas (padrão: 1)", example = "3")
    private Integer numeroParcelas;

    public PagamentoRequestDTO() {
    }

    public PagamentoRequestDTO(MetodoPagamento metodo, BigDecimal valor, Integer numeroParcelas) {
        this.metodo = metodo;
        this.valor = valor;
        this.numeroParcelas = numeroParcelas;
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
}