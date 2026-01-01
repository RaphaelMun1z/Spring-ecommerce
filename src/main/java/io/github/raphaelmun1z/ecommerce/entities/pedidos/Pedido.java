package io.github.raphaelmun1z.ecommerce.entities.pedidos;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataPedido = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPedido status = StatusPedido.AGUARDANDO_PAGAMENTO;

    @NotNull
    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "valor_desconto", precision = 10, scale = 2)
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Entrega entrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Pagamento pagamento;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    public Pedido() {
    }

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
        calculaTotal();
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
        if (entrega != null) {
            entrega.setPedido(this);
        }
        calculaTotal();
    }

    public void calculaTotal() {
        BigDecimal totalItens = itens.stream()
            .map(ItemPedido::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal frete = (entrega != null && entrega.getValorFrete() != null)
            ? entrega.getValorFrete()
            : BigDecimal.ZERO;

        BigDecimal desconto = valorDesconto != null ? valorDesconto : BigDecimal.ZERO;

        this.valorTotal = totalItens.add(frete).subtract(desconto);

        if (this.valorTotal.compareTo(BigDecimal.ZERO) < 0) {
            this.valorTotal = BigDecimal.ZERO;
        }
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
        calculaTotal();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }
}