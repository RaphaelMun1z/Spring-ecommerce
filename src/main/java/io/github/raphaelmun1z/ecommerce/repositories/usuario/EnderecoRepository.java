package io.github.raphaelmun1z.ecommerce.repositories.usuario;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {
    List<Endereco> findByClienteId(String clienteId);

    @Modifying
    @Query("UPDATE Endereco e SET e.principal = false WHERE e.cliente.id = :clienteId")
    void resetarPrincipal(String clienteId);
}