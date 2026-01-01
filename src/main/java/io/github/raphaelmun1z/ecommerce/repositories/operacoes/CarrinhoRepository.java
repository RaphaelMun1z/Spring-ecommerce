package io.github.raphaelmun1z.ecommerce.repositories.operacoes;

import io.github.raphaelmun1z.ecommerce.entities.carrinho.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, String> {
    Optional<Carrinho> findByClienteId(String clienteId);
}