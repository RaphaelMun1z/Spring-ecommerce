package io.github.raphaelmun1z.ecommerce.services.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoMovimentacao;
import io.github.raphaelmun1z.ecommerce.entities.estoque.MovimentacaoEstoque;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.EstoqueRepository;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoque> findHistoricoByProduto(String produtoId, Pageable pageable) {
        if (!produtoRepository.existsById(produtoId)) {
            throw new EntityNotFoundException("Produto não encontrado. Id: " + produtoId);
        }
        return estoqueRepository.findByProdutoId(produtoId, pageable);
    }

    @Transactional
    public MovimentacaoEstoque registrarMovimentacao(MovimentacaoEstoque movimentacao) {
        Produto produto = produtoRepository.findById(movimentacao.getProduto().getId())
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado para movimentação."));

        if (movimentacao.getQuantidade() == null || movimentacao.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade movimentada deve ser maior que zero.");
        }

        if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA) {
            produto.setEstoque(produto.getEstoque() + movimentacao.getQuantidade());
        } else if (movimentacao.getTipo() == TipoMovimentacao.SAIDA) {
            if (produto.getEstoque() < movimentacao.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para realizar a saída.");
            }
            produto.setEstoque(produto.getEstoque() - movimentacao.getQuantidade());
        }

        produtoRepository.save(produto);
        movimentacao.setProduto(produto);
        return estoqueRepository.save(movimentacao);
    }
}