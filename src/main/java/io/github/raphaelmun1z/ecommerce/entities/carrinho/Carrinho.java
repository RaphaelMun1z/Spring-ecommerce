package io.github.raphaelmun1z.ecommerce.entities.carrinho;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_carrinho")
@Schema(description = "Entidade que representa o carrinho de compras ativo de um cliente")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único do carrinho", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", unique = true)
    @Schema(description = "Cliente proprietário deste carrinho")
    private Cliente cliente;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de produtos adicionados ao carrinho")
    private List<ItemCarrinho> itens = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    @Schema(description = "Data e hora de criação do carrinho")
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    @Schema(description = "Data e hora da última alteração no carrinho")
    private LocalDateTime dataAtualizacao;

    public Carrinho() {
    }

    public Carrinho(Cliente cliente) {
        this.cliente = cliente;
    }

    public void adicionarItem(ItemCarrinho item) {
        itens.add(item);
        item.setCarrinho(this);
    }

    public void removerItem(ItemCarrinho item) {
        itens.remove(item);
        item.setCarrinho(null);
    }

    @Transient
    @Schema(description = "Valor total calculado dos itens no carrinho", example = "299.90", accessMode = Schema.AccessMode.READ_ONLY)
    public BigDecimal getTotal() {
        return itens.stream()
            .map(ItemCarrinho::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
}