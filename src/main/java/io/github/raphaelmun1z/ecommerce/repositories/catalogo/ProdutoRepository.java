package io.github.raphaelmun1z.ecommerce.repositories.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProdutoRepository extends JpaRepository<Produto, String>, JpaSpecificationExecutor<Produto> {

    boolean existsByCodigoControle(String codigoControle);

    // Métodos antigos mantidos para compatibilidade, mas o findAll(Specification, Pageable) substitui a maioria
    Page<Produto> findByAtivoTrue(Pageable pageable);
}