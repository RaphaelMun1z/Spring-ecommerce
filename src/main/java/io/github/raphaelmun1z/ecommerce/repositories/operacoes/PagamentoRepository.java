package io.github.raphaelmun1z.ecommerce.repositories.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, String> {
    Optional<Pagamento> findByCodigoTransacaoGateway(String codigoTransacao);
}