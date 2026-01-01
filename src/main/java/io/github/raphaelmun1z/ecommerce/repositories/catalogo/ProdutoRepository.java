package io.github.raphaelmun1z.ecommerce.repositories.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoRepository extends JpaRepository<Produto, String> {
    boolean existsByCodigoControle(String codigoControle);

    Page<Produto> findByAtivoTrue(Pageable pageable);

    @Query("SELECT p FROM Produto p WHERE p.ativo = true AND " +
        "(LOWER(p.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
        "LOWER(p.descricao) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<Produto> search(@Param("termo") String termo, Pageable pageable);
}
