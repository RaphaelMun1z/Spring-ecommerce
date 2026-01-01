package io.github.raphaelmun1z.ecommerce.repositories.usuario;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByEmail(String email);
}
