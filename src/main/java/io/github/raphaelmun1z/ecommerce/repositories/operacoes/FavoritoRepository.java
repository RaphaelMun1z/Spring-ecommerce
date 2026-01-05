package io.github.raphaelmun1z.ecommerce.repositories.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.carrinho.Favorito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, String> {

    Page<Favorito> findByClienteId(String clienteId, Pageable pageable);

    boolean existsByClienteIdAndProdutoId(String clienteId, String produtoId);

    Optional<Favorito> findByClienteIdAndProdutoId(String clienteId, String produtoId);

    void deleteByClienteId(String clienteId);
}