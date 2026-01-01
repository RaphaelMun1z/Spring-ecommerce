package io.github.raphaelmun1z.ecommerce.entities.carrinho;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
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
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @CreationTimestamp
    @Column(name = "data_adicao", updatable = false)
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