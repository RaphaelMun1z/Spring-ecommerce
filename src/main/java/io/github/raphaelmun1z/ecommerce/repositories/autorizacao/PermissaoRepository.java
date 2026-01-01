package io.github.raphaelmun1z.ecommerce.repositories.autorizacao;

import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissaoRepository extends JpaRepository<Permissao, String> {
    Optional<Permissao> findByNome(String nomePermissao);
}
