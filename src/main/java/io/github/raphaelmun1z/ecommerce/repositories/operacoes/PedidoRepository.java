package io.github.raphaelmun1z.ecommerce.repositories.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, String> {
    Page<Pedido> findByClienteId(String clienteId, Pageable pageable);

    Page<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);

    Optional<Pedido> findByPagamento_BillingId(String billingId);
}