package io.github.raphaelmun1z.ecommerce.repositories.usuario;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByEmail(String email);
}
