package io.github.raphaelmun1z.ecommerce.repositories.usuario;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    @EntityGraph(attributePaths = {"papel", "papel.permissoes"})
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.papel p JOIN FETCH p.permissoes WHERE u.email = :email")
    Optional<Usuario> findByEmailWithRolesAndPermissions(@Param("email") String email);

    Optional<Usuario> findByPasswordResetToken(@NotBlank(message = "O token de redefinição é obrigatório.") String token);

    boolean existsByPapel_Id(String id);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Usuario> buscarPorNomeOuEmail(@Param("termo") String termo, Pageable pageable);
}