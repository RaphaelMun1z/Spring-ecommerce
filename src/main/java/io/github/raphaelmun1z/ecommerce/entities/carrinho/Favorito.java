package io.github.raphaelmun1z.ecommerce.entities.carrinho;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_favorito",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cliente_id", "produto_id"})
    },
    indexes = {
        @Index(name = "idx_favorito_cliente", columnList = "cliente_id")
    }
)
@Schema(description = "Entidade que representa um item na lista de desejos (favoritos) do cliente")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único do registro de favorito", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    @Schema(description = "Cliente que adicionou o item aos favoritos")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    @Schema(description = "Produto favoritado")
    private Produto produto;

    @CreationTimestamp
    @Column(name = "data_adicao", updatable = false)
    @Schema(description = "Data e hora da adição aos favoritos")
    private LocalDateTime dataAdicao;

    public Favorito() {
    }

    public Favorito(Cliente cliente, Produto produto) {
        this.cliente = cliente;
        this.produto = produto;
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

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public LocalDateTime getDataAdicao() {
        return dataAdicao;
    }
}