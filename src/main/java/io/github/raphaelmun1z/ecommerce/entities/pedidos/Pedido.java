package io.github.raphaelmun1z.ecommerce.entities.pedidos;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidade que representa um pedido de compra realizado por um cliente")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único do pedido", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @NotNull
    @Column(nullable = false)
    @Schema(description = "Data e hora em que o pedido foi criado")
    private LocalDateTime dataPedido = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Schema(description = "Status atual do ciclo de vida do pedido", example = "AGUARDANDO_PAGAMENTO")
    private StatusPedido status = StatusPedido.AGUARDANDO_PAGAMENTO;

    @NotNull
    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Valor total final do pedido (itens + frete - descontos)", example = "350.00")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "valor_desconto", precision = 10, scale = 2)
    @Schema(description = "Valor total de descontos aplicados", example = "10.00")
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    @Schema(description = "Cliente que realizou o pedido")
    private Cliente cliente;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Dados logísticos da entrega vinculada ao pedido")
    private Entrega entrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de itens comprados neste pedido")
    private List<ItemPedido> itens = new ArrayList<>();

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    @Schema(description = "Dados do pagamento vinculado ao pedido")
    private Pagamento pagamento;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    @Schema(description = "Data e hora de criação do registro")
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    @Schema(description = "Data e hora da última atualização do registro")
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

    // --- Getters e Setters ---

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

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
        if (entrega != null) {
            entrega.setPedido(this);
        }
        calculaTotal();
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
        if (pagamento != null) {
            pagamento.setPedido(this);
        }
    }

    public List<ItemPedido> getItens() {
        return itens;
    }
}