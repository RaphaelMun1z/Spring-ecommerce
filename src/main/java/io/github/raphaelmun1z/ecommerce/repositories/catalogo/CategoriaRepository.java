package io.github.raphaelmun1z.ecommerce.repositories.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, String> {
    boolean existsByNome(String nome);

    boolean existsBySlug(String slug);

    Page<Categoria> findByAtivaTrue(Pageable pageable);
}