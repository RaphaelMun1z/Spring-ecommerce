package io.github.raphaelmun1z.ecommerce.repositories.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.estoque.MovimentacaoEstoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EstoqueRepository extends JpaRepository<MovimentacaoEstoque, String> {
    Page<MovimentacaoEstoque> findByProdutoId(String produtoId, Pageable pageable);

    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.produto.id = :produtoId AND m.tipo = 'ENTRADA'")
    Page<MovimentacaoEstoque> findEntradasByProdutoId(@Param("produtoId") String produtoId, Pageable pageable);

    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.produto.id = :produtoId AND m.tipo = 'SAIDA'")
    Page<MovimentacaoEstoque> findSaidasByProdutoId(@Param("produtoId") String produtoId, Pageable pageable);
}