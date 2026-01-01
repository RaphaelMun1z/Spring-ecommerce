package io.github.raphaelmun1z.ecommerce.repositories.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.pedidos.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntregaRepository extends JpaRepository<Entrega, String> {

    Optional<Entrega> findByPedidoId(String pedidoId);

    boolean existsByCodigoRastreio(String codigoRastreio);
}