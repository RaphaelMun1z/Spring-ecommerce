package io.github.raphaelmun1z.ecommerce.repositories.analitico;

import io.github.raphaelmun1z.ecommerce.entities.Notificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository extends JpaRepository<Notificacao, String> {
    Page<Notificacao> findByClienteId(String clienteId, Pageable pageable);

    Page<Notificacao> findByDestinatario(String destinatario, Pageable pageable);
}