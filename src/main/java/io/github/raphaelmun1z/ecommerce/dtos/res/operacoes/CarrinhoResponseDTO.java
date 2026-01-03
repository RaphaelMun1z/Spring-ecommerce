package io.github.raphaelmun1z.ecommerce.dtos.res.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.carrinho.Carrinho;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO contendo os dados completos do carrinho de compras")
public class CarrinhoResponseDTO {

    @Schema(description = "Identificador único do carrinho", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "ID do cliente proprietário", example = "550e8400-e29b-41d4-a716-446655440000")
    private String clienteId;

    @Schema(description = "Lista de itens adicionados ao carrinho")
    private List<ItemCarrinhoResponseDTO> itens = new ArrayList<>();

    @Schema(description = "Valor total do carrinho", example = "1250.90")
    private BigDecimal total;

    @Schema(description = "Data da última atualização", example = "2024-03-20T10:30:00")
    private LocalDateTime dataAtualizacao;

    public CarrinhoResponseDTO() {
    }

    public CarrinhoResponseDTO(Carrinho carrinho) {
        this.id = carrinho.getId();
        this.clienteId = (carrinho.getCliente() != null) ? carrinho.getCliente().getId() : null;
        this.total = carrinho.getTotal();
        this.dataAtualizacao = carrinho.getDataAtualizacao();

        if (carrinho.getItens() != null && !carrinho.getItens().isEmpty()) {
            this.itens = carrinho.getItens().stream()
                .map(ItemCarrinhoResponseDTO::new)
                .collect(Collectors.toList());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemCarrinhoResponseDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinhoResponseDTO> itens) {
        this.itens = itens;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}